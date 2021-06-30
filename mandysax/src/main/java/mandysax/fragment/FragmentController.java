package mandysax.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.livedata.MutableLiveData;

/*
 *将对Fragment的各种操作都放在FragmentController中
 *FragmentController2相当于起了记录作用
 *将各种操作从之前的FragmentManager中分离
 */
public final class FragmentController extends ViewModel implements FragmentControllerImpl, LifecycleObserver {

    @Override
    public void Observer(Lifecycle.Event state) {
        if (mActivityCreated.getValue() == null)
            if (state == Lifecycle.Event.ON_START)
                mActivityCreated.setValue(true);
    }

    enum STACK {
        SHOW,
        HIDE,
        REMOVE,
        REPLACE,
        ADD
    }

    static final class Op {
        int id;
        String tag;
        STACK stack;
        Fragment fragment;
        int enterAnim;
        int exitAnim;
        int popEnterAnim;
        int popExitAnim;
        boolean isAddToBackStack;
        ArrayList<Fragment> removed;
    }

    public class BackStackRecord {

        private final ArrayList<Op> mActive;

        public BackStackRecord(ArrayList<Op> opl) {
            mActive = opl;
        }

        public Fragment get(int index) {
            return mActive.get(index).fragment;
        }

        public int size() {
            return mActive.size();
        }

        public void run() {
            /*run*/
            for (Op op : mActive) {
                moveToStack(op);
            }
        }

        public void rollback() {
            /*rollback*/
            for (final Op op : mActive) {
                //fix bug form 2.1.0
                switch (op.stack) {
                    case ADD:
                    case SHOW:
                        hide(op.fragment, op.popExitAnim);
                        break;
                    case HIDE:
                        show(op.fragment, op.popEnterAnim);
                        break;
                    case REMOVE:
                        break;
                    case REPLACE:
                        if (op.isAddToBackStack) {
                            op.fragment.getLifecycle().addObserver((Lifecycle.Event state) -> {
                                if (state == Lifecycle.Event.ON_STOP && op.fragment.isHidden()) {
                                    remove(op.fragment);
                                }
                            });
                            hide(op.fragment, op.popExitAnim);
                            if (op.removed != null)
                                for (Fragment fragment : op.removed) {
                                    show(fragment, op.enterAnim);
                                }
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + op.stack);
                }
            }
        }

        public void moveToStack(Op op) {
            switch (op.stack) {
                case ADD:
                    add(op.fragment, op.id, op.tag);
                    break;
                case SHOW:
                    show(op.fragment, op.enterAnim);
                    break;
                case HIDE:
                    hide(op.fragment, op.exitAnim);
                    break;
                case REPLACE:
                    replace(op);
                    break;
                case REMOVE:
                    remove(op.fragment);
                    break;
            }
        }

        public void replace(Op op) {
            for (Fragment fragment : getFragmentManager().getFragmentList()) {
                if (fragment.getRoot() == null)
                    continue;
                if (fragment.getRoot().getParent() == null)
                    continue;
                if (((ViewGroup) fragment.getRoot().getParent()).getId() == op.id) {
                    if (!fragment.isVisible())
                        continue;
                    if (!op.isAddToBackStack) {
                        fragment.getLifecycle().addObserver(state -> {
                            if (state != Lifecycle.Event.ON_STOP) {
                                return;
                            }
                            remove(fragment);
                        });
                    } else {
                        if (op.removed == null)
                            op.removed = new ArrayList<>();
                        op.removed.add(fragment);
                    }
                    hide(fragment, op.exitAnim);

                }
            }

            add(op.fragment, op.id, op.tag);
            show(op.fragment, op.popEnterAnim);
        }

        public void add(Fragment fragment, int id, String tag) {
            tag = tag == null ? fragment.getClass().getCanonicalName() : tag;
            if (fragment.isAdded()) {
                throw new RuntimeException("Fragment has been added");
            } else {
                getFragmentManager().addFragment(fragment);
                fragment.set(FragmentController.this, tag, id);
                fragment._onAttach(mActivity);
                fragment._onCreate(mSavedInstanceState);
            }
            if (!fragment.isInLayout() && id != 0) {
                View view1 = fragment.onCreateView(LayoutInflater.from(mActivity.getBaseContext()), fragment.getViewGroup());
                View view2 = null;
                if (fragment.onCreateView() != 0)
                    view2 = fragment.getLayoutInflater().inflate(fragment.onCreateView(), fragment.getViewGroup(), false);
                fragment._onViewCreated(view1 == null ? view2 : view1, mSavedInstanceState);
                mActivityCreated.lazy(p1 -> fragment.onActivityCreated(mSavedInstanceState));
            }
        }

        public void remove(Fragment fragment) {
            new Handler(Looper.getMainLooper()).post(() -> {
                fragment._onDestroyView();
                fragment._onDestroy();
                fragment._onDetach();
                getFragmentManager().removeFragment(fragment);
            });
        }

        public void show(Fragment fragment, int anim) {
            //fix bug form v2.1.0
            if (fragment.isAdded())
                if (fragment.getRoot() != null) {
                    View view = fragment.getRoot();
                    if (anim != 0) {
                        Animation startAnim = AnimationUtils.loadAnimation(fragment.getContext(), anim);
                        startAnim.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation p1) {
                                view.setVisibility(View.VISIBLE);
                                fragment._onStart();
                            }

                            @Override
                            public void onAnimationEnd(Animation p1) {
                                fragment._onResume();
                            }

                            @Override
                            public void onAnimationRepeat(Animation p1) {
                            }
                        });
                        view.startAnimation(startAnim);
                    } else {
                        view.setVisibility(View.VISIBLE);
                        fragment._onResume();
                    }
                }
        }

        public void hide(final Fragment fragment, final int anim) {
            //fix bug form v2.1.0
            if (fragment.isAdded())
                if (fragment.getRoot() != null) {
                    final View view = fragment.getRoot();
                    if (anim != 0) {
                        Animation exitAnim = AnimationUtils.loadAnimation(fragment.getContext(), anim);
                        exitAnim.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation p1) {
                                fragment._onPause();
                            }

                            @Override
                            public void onAnimationEnd(Animation p1) {
                                view.setVisibility(View.GONE);
                                fragment._onStop();
                            }

                            @Override
                            public void onAnimationRepeat(Animation p1) {
                            }
                        });
                        view.startAnimation(exitAnim);
                    } else {
                        view.setVisibility(View.GONE);
                        fragment._onStop();
                    }
                }

        }

    }

    public class BackStack {

        private ArrayList<OnBackStackChangedListener> mBackListeners;

        private ArrayList<BackStackRecord> mBackStack;

        private ArrayList<Integer> mBackStackIndices;//存储可用的下标

        public BackStack() {
        }

        public int allocBackStackIndex(BackStackRecord bse) {
            if (mBackStack == null)
                mBackStack = new ArrayList<>();
            if (mBackStackIndices == null)
                mBackStackIndices = new ArrayList<>();
            for (int i = 0; i < mBackStackIndices.size(); i++) {
                //等于null时代表这里是个空位
                if (mBackStackIndices.get(i) == null) {
                    mBackStack.set(i, bse);//利用这个空位，避免list经常扩容
                    mBackStackIndices.remove(i);//移出可用的下标
                    return i;
                }
            }
            mBackStack.add(bse);
            mBackStackIndices.add(mBackStackIndices.size());
            return mBackStackIndices.size() - 1;
        }

        //消费指定返回栈，并添加到可用的下标
        public void freeBackStackIndex(int index) {
            mBackStack.set(index, null);
            mBackStackIndices.add(index);
        }

        //获取当前返回栈中fragment个数
        public int getBackStackEntryCount() {
            if (mBackStack == null) return 0;
            return mBackStack.get(mBackStack.size() - 1).size();
        }

        public Fragment getBackStackEntryAt(int index) {
            if (mBackStack == null) return null;
            return mBackStack.get(mBackStack.size() - 1).get(index);
        }

        //弹出指定返回栈
        public boolean popBackStack(int index) {
            if (mBackStack == null) return false;
            if (mBackStack.size() >= 1) {
                BackStackRecord bsr = mBackStack.get(index);
                if (bsr == null) return false;
                bsr.rollback();
                freeBackStackIndex(index);
                return true;
            }
            return false;
        }

        //弹出栈顶
        public boolean popBackStack() {
            if (mBackStack != null)
                return popBackStack(mBackStack.size() - 1);
            return false;
        }

        @Deprecated
        public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
            mBackListeners.add(listener);
        }

        @Deprecated
        public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
            mBackListeners.remove(listener);
        }

    }

    private final BackStack mFragmentBackStack = new BackStack();

    public class FragmentController2 implements FragmentController2Impl {

        private Fragment mFragment;

        //这个是为了避免在fragment中添加子fragment，删除fragment后，子fragment没有被移除导致的内存泄露问题
        @Override
        public FragmentController2Impl getFragmentPlusManager(Fragment fragment) {
            if (findFragmentByTag(fragment.getTag()) == null)
                throw new RuntimeException("fragment has been removed or not added");
            mFragment = fragment;
            return this;
        }

        @Override
        public boolean popBackStack() {
            return mFragmentBackStack.popBackStack();
        }

        @Override
        public boolean popBackStack(int index) {
            return mFragmentBackStack.popBackStack(index);
        }

        @Override
        public int getBackStackEntryCount() {
            return mFragmentBackStack.getBackStackEntryCount();
        }

        @Override
        public Fragment getBackStackEntryAt(int index) {
            return mFragmentBackStack.getBackStackEntryAt(index);
        }

        private final ArrayList<Op> mOpl = new ArrayList<>();

        private int mIndex = 0;//这个变量用来标记上次执行添加返回栈后的下标

        @Override
        public FragmentController2Impl removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
            mFragmentBackStack.removeOnBackStackChangedListener(listener);
            return this;
        }

        @Override
        public FragmentController2Impl addOnBackStackChangedListener(OnBackStackChangedListener listener) {
            mFragmentBackStack.addOnBackStackChangedListener(listener);
            return this;
        }

        private int
                mEnterAnim,
                mExitAnim,
                mPopEnterAnim,
                mPopExitAnim;

        private boolean mIsAddBackStack = false;//记录有没有添加到返回栈

        /*
         *这个变量用于记录add操作时的Fragment
         *系统Fragment在这个步骤默认已经向容器添加了Fragment而我并没有这么做，这样在add后的show(Fragment fragment)操作繁琐，就添加了无参的show()
         *这样能更好的控制Fragment，因为有的Fragment并没有界面，默认添加到容器有些限制Fragment的操作上限
         */
        //上面这句话在v2.1.0版本中被收回了
        public void addOp(int id, Fragment fragment, STACK stack, String tag) {
            Op op = new Op();
            op.id = id;
            op.fragment = fragment;
            op.stack = stack;
            op.tag = tag;
            op.enterAnim = mEnterAnim;
            op.exitAnim = mExitAnim;
            op.popEnterAnim = mPopEnterAnim;
            op.popExitAnim = mPopExitAnim;
            mOpl.add(op);
        }

        @Override
        public <T extends Fragment> T findFragmentByTag(String tag) {
            return (T) getFragmentManager().tagGetFragment(tag);
        }

        @Override
        public <T extends Fragment> T findFragmentById(int id) {
            FragmentView fcv = getActivity().findViewById(id);
            return fcv != null ? (T) fcv.getFragment() : null;
        }

        @Override
        public FragmentController2Impl setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim) {
            mEnterAnim = enterAnim;
            mExitAnim = exitAnim;
            mPopEnterAnim = popEnterAnim;
            mPopExitAnim = popExitAnim;
            return this;
        }

        @Override
        public FragmentController2Impl add(int id, Fragment fragment) {
            return add(id, fragment, null);
        }

        @Override
        public FragmentController2Impl add(int id, Fragment fragment, String tag) {
            addOp(id, fragment, STACK.ADD, tag);
            return show(fragment);//2.1.1新增
        }

        @Override
        public FragmentController2Impl remove(Fragment fragment) {
            addOp(0, fragment, STACK.REMOVE, null);
            return this;
        }

        @Override
        public FragmentController2Impl show(Fragment fragment) {
            addOp(0, fragment, STACK.SHOW, null);
            return this;
        }

        @Override
        public FragmentController2Impl hide(Fragment fragment) {
            addOp(0, fragment, STACK.HIDE, null);
            return this;
        }

        @Override
        public FragmentController2Impl replace(int id, Fragment replaceFragment) {
            replace(id, replaceFragment, null);
            return this;
        }

        @Override
        public FragmentController2Impl replace(int id, Fragment replaceFragment, String tag) {
            addOp(id, replaceFragment, STACK.REPLACE, tag);
            return this;
        }

        @Override
        public FragmentController2Impl replace(int id, Class replaceFragment) {
            return replace(id, replaceFragment, replaceFragment.getCanonicalName());
        }

        @Override
        public FragmentController2Impl replace(int id, Class replaceFragment, String tag) {
            try {
                replace(id, (Fragment) replaceFragment.newInstance(), tag);
            } catch (IllegalAccessException | InstantiationException ignored) {
            }
            return this;
        }

        @Override
        public FragmentController2Impl addToBackStack() {
            mIsAddBackStack = true;
            for (; mIndex < mOpl.size(); mIndex++) {
                mOpl.get(mIndex).isAddToBackStack = true;
            }
            return this;
        }

        @Override
        public int commit() {
            final BackStackRecord bsr = new BackStackRecord(mOpl);
            mActivityCreated.lazy(p1 -> bsr.run());
            return mIsAddBackStack ? mFragmentBackStack.allocBackStackIndex(bsr) : -1;
        }

        @Override
        public int commitNow() {
            BackStackRecord bsr = new BackStackRecord(mOpl);
            bsr.run();
            return mIsAddBackStack ? mFragmentBackStack.allocBackStackIndex(bsr) : -1;
        }

    }

    private final FragmentManager mManager;

    private Bundle mSavedInstanceState;

    private FragmentActivity mActivity;

    private final MutableLiveData<Boolean> mActivityCreated = new MutableLiveData<>();//用来确保activity已经构建完毕

    public FragmentController(FragmentActivity activity) {
        mManager = new FragmentManager();
        mActivity = activity;
    }

    private FragmentManagerImpl getFragmentManager() {
        return mManager;
    }

    public FragmentActivity getActivity() {
        return mActivity;
    }

    @Override
    public boolean onBackFragment() {
        for (Fragment fragment : getFragmentManager().getFragmentList()) {
            if (fragment.isVisible())
                if (fragment.onBackPressed())
                    return true;
        }
        return false;
    }

    @Override
    public FragmentController2Impl getFragmentController2() {
        return new FragmentController2();
    }

    /*
     *控制器的生命周期
     */

    @Override
    public void resumeFragment() {
        for (final Fragment fragment : getFragmentManager().getFragmentList()) {
            if (fragment.isAdded() && fragment.isInLayout()) {
                //System.out.println("显示:"+fragment.toString());
                if (fragment.getRoot() != null && fragment.getViewGroup() != null)
                    fragment.getViewGroup().addView(fragment.getRoot());
                if (fragment.isVisible())
                    getFragmentController2().show(fragment).commit();
            }
            mActivityCreated.lazy(p1 -> fragment.onActivityCreated(mSavedInstanceState));
        }
    }

    @Override
    public void create(final FragmentActivity activity, Bundle activitySavedInstanceState) {
        mActivity = activity;
        mSavedInstanceState = activitySavedInstanceState;
        for (Fragment fragment : getFragmentManager().getFragmentList()) {
            fragment._onAttach(mActivity);
        }
        activity.getLifecycle().addObserver(this);
        activity.getLifecycle().addObserver(state -> {
            for (Fragment fragment : getFragmentManager().getFragmentList()) {
                //只有显示的Fragment才可以接收到生命周期事件
                switch (state) {
                    case ON_START:
                        if (fragment.isVisible())
                            fragment._onStart();
                        break;
                    case ON_RESTART:
                        if (fragment.isVisible())
                            fragment._onRestart();
                        break;
                    case ON_RESUME:
                        if (fragment.isVisible())
                            fragment._onResume();
                        break;
                    case ON_PAUSE:
                        if (fragment.isVisible())
                            fragment._onPause();
                        break;
                    case ON_STOP:
                        if (fragment.isVisible())
                            fragment._onStop();
                        break;
                    case ON_DESTROY:
                        fragment._onDestroyView();
                        fragment._onDestroy();
                        fragment._onDetach();
                        break;
                }
            }
            if (state == Lifecycle.Event.ON_DESTROY) {
                mSavedInstanceState = null;
                mActivity = null;
                mActivityCreated.setValue(null);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        getFragmentManager().clear();
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        for (Fragment fragment : getFragmentManager().getFragmentList())
            fragment.onSaveInstanceState(outState);
    }

    @Override
    public void multiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        for (Fragment fragment : getFragmentManager().getFragmentList())
            fragment.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
    }

    @Override
    public void configurationChanged(Configuration newConfig) {
        for (Fragment fragment : getFragmentManager().getFragmentList())
            fragment.onConfigurationChanged(newConfig);
    }

    /*
     *控制器的生命周期
     */

}
