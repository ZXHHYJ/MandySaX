package mandysax.fragment;

import android.view.View;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import mandysax.core.app.ComponentActivity;
import mandysax.core.app.OnBackPressedCallback;

/**
 * @author liuxiaoliu66
 */
public final class FragmentController extends FragmentStateManager {

    private final BackStack mFragmentBackStack = new BackStack();

    public class BackStack {

        /**
         * 存储返回栈更新的监听
         */
        private CopyOnWriteArrayList<OnBackStackChangedListener> mBackListeners;

        /**
         * 存储返回栈
         */
        private ArrayList<FragmentController.BackStackRecord> mBackStack;

        /**
         * 存储可用的下标
         */
        private ArrayList<Integer> mBackStackIndices;

        public int allocBackStackIndex(final FragmentController.BackStackRecord bsr) {
            if (mBackStack == null) {
                mBackStack = new ArrayList<>();
            }
            if (mBackStackIndices == null) {
                mBackStackIndices = new ArrayList<>();
            }
            ComponentActivity.OnBackPressedDispatcher onBackPressedDispatcher = mActivity.getOnBackPressedDispatcher();
            onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {

                @Override
                public void handleOnBackPressed() {
                    //判断此返回栈是否已经被销毁，如果已被销毁需要再onBackPressed一次，否则相当于吞了一次onBackPressed
                    if (mBackStack.contains(bsr)) {
                        mFragmentBackStack.popBackStack(mBackStack.lastIndexOf(bsr));
                    } else {
                        remove();
                        onBackPressedDispatcher.onBackPressed();
                    }
                }

            });
            for (int i = 0; i < mBackStackIndices.size(); i++) {
                //NonNull时代表这里是个空位
                Integer index = mBackStackIndices.get(i);
                if (index != null) {
                    mBackStack.set(index, bsr);//利用这个空位，避免list经常扩容
                    mBackStackIndices.remove(index);//移出可用的下标
                    dumpBsc();
                    return index;
                }
            }
            mBackStack.add(bsr);
            dumpBsc();
            return mBackStack.size() - 1;
        }

        /**
         * 消费指定返回栈，并添加到可用的下标
         *
         * @param index 带消费的返回栈下标
         */
        public void freeBackStackIndex(int index) {
            mBackStack.set(index, null);
            mBackStackIndices.add(index);
        }

        /**
         * 获取当前返回栈大小
         *
         * @return 返回栈大小
         */
        public int getBackStackSize() {
            if (mBackStack == null || mBackStackIndices == null) {
                return 0;
            }
            return mBackStack.size() - mBackStackIndices.size();
        }

        /**
         * 弹出指定返回栈
         *
         * @param index 待弹出返回栈的下表
         * @return 是否弹出成功
         */
        public boolean popBackStack(int index) {
            if (mBackStack == null) {
                return false;
            }
            if (getBackStackSize() >= 1) {
                FragmentController.BackStackRecord bsr = mBackStack.get(index);
                freeBackStackIndex(index);
                if (bsr != null) {
                    bsr.rollback();
                    dumpBsc();
                    return true;
                }
            }
            return false;
        }

        /**
         * 弹出栈顶
         *
         * @return 是否弹出成功
         */
        public boolean popBackStack() {
            if (mBackStack != null) {
                return popBackStack(getBackStackSize() - 1);
            }
            return false;
        }

        /**
         * 添加返回栈更新监听器
         *
         * @param listener 待添加的监听器
         */
        public void addOnBackStackChangedListener(@NonNull FragmentController.OnBackStackChangedListener listener) {
            if (mBackListeners == null) {
                mBackListeners = new CopyOnWriteArrayList<>();
            }
            mBackListeners.add(listener);
        }

        /**
         * 移除已添加的返回栈监听器
         *
         * @param listener 待移除的监听器
         */
        public void removeOnBackStackChangedListener(@NonNull FragmentController.OnBackStackChangedListener listener) {
            if (mBackListeners == null) {
                mBackListeners = new CopyOnWriteArrayList<>();
            }
            mBackListeners.remove(listener);
        }

        private void dumpBsc() {
            if (mBackListeners == null) {
                return;
            }
            for (FragmentController.OnBackStackChangedListener listener : mBackListeners) {
                listener.onBackStackChanged();
            }
        }

    }


    /**
     * 为指定的Fragment生成FragmentManager
     *
     * @param fragment 当前Fragment
     * @return FragmentManager
     */
    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public final FragmentManager getFragmentManager(Fragment fragment) {
        return new FragmentManagerImpl(fragment);
    }

    enum STACK {
        /**
         * 显示
         */
        SHOW,
        /**
         * 隐藏
         */
        HIDE,
        /**
         * 移除
         */
        REMOVE,
        /**
         * 切换
         */
        REPLACE,
        /**
         * 添加
         */
        ADD
    }

    /**
     * 监听FragmentManager返回栈变更
     */
    public interface OnBackStackChangedListener {
        void onBackStackChanged();
    }

    public class BackStackRecord {

        private final ArrayList<Op> mActive;

        public BackStackRecord(ArrayList<Op> opl) {
            mActive = opl;
        }

        /**
         * 运行
         */
        public void run() {
            for (Op op : mActive) {
                moveToStack(op);
            }
        }

        /**
         * 回退
         */
        public void rollback() {
            for (Op op : mActive) {
                //fix bug form 2.1.0
                switch (op.stack) {
                    case ADD:
                    case SHOW:
                        dispatchHide(op.fragment, op.popExitAnim);
                        break;
                    case HIDE:
                        dispatchShow(op.fragment, op.popEnterAnim);
                        break;
                    case REMOVE:
                        break;
                    case REPLACE:
                        if (op.isAddToBackStack) {
                            dispatchRemove(op.fragment, op.popExitAnim);
                            if (op.removed != null) {
                                for (int i = op.removed.size() - 1; i >= 0; i--) {
                                    dispatchShow(op.removed.get(i), op.popEnterAnim);
                                }
                            }
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + op.stack);
                }
            }
        }

        public void moveToStack(@NonNull Op op) {
            switch (op.stack) {
                case ADD:
                    dispatchAdd(op.parentFragment, op.fragment, op.id, op.tag);
                    break;
                case SHOW:
                    dispatchShow(op.fragment, op.enterAnim);
                    break;
                case HIDE:
                    dispatchHide(op.fragment, op.exitAnim);
                    break;
                case REPLACE:
                    dispatchReplace(op);
                    break;
                case REMOVE:
                    dispatchRemove(op.fragment, op.popExitAnim);
                    break;
                default:
            }
        }

    }

    private class FragmentTransactionImpl implements FragmentTransaction {

        private final ArrayList<Op> mOpl = new ArrayList<>();

        private int
                mEnterAnim,
                mExitAnim,
                mPopEnterAnim,
                mPopExitAnim;
        //记录有没有添加到返回栈
        private boolean mAddBackStack = false;
        //是否已经提交
        private boolean mCommitted = false;
        //这个变量用来标记上次执行添加返回栈后的下标
        private int mIndex = 0;

        //父Fragment
        private final Fragment mParentFragment;

        public FragmentTransactionImpl() {
            mParentFragment = null;
        }

        public FragmentTransactionImpl(Fragment parentFragment) {
            mParentFragment = parentFragment;
        }

        /*
         *这个变量用于记录add操作时的Fragment
         *系统Fragment在这个步骤默认已经向容器添加了Fragment而我并没有这么做，这样在add后的show(Fragment fragment)操作繁琐，就添加了无参的show()
         *这样能更好的控制Fragment，因为有的Fragment并没有界面，默认添加到容器有些限制Fragment的操作上限
         */
        //上面这句话在v2.1.0版本中被收回了
        public void addOp(int id, Fragment fragment, STACK stack, String tag) {
            if (fragment == null) {
                throw new NullPointerException("fragment cannot be null");
            }
            Op op = new Op();
            op.id = id;
            op.parentFragment = mParentFragment;
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
        public FragmentTransaction setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim) {
            mEnterAnim = enterAnim;
            mExitAnim = exitAnim;
            mPopEnterAnim = popEnterAnim;
            mPopExitAnim = popExitAnim;
            return this;
        }

        @Override
        public FragmentTransaction add(int id, Fragment fragment) {
            return add(id, fragment, null);
        }

        @Override
        public FragmentTransaction add(int id, Fragment fragment, String tag) {
            addOp(id, fragment, STACK.ADD, tag);
            return show(fragment);//2.1.1新增
        }

        @Override
        public FragmentTransaction remove(Fragment fragment) {
            addOp(0, fragment, STACK.REMOVE, null);
            return this;
        }

        @Override
        public FragmentTransaction show(Fragment fragment) {
            addOp(0, fragment, STACK.SHOW, null);
            return this;
        }

        @Override
        public FragmentTransaction hide(Fragment fragment) {
            addOp(0, fragment, STACK.HIDE, null);
            return this;
        }

        @Override
        public FragmentTransaction replace(int id, Fragment replaceFragment) {
            replace(id, replaceFragment, null);
            return this;
        }

        @Override
        public FragmentTransaction replace(int id, Fragment replaceFragment, String tag) {
            addOp(id, replaceFragment, STACK.REPLACE, tag);
            return this;
        }

        @Override
        public FragmentTransaction replace(int id, Class<? super Fragment> replaceFragment) {
            return replace(id, replaceFragment, replaceFragment.getCanonicalName());
        }

        @Override
        public FragmentTransaction replace(int id, @NonNull Class<? super Fragment> replaceFragment, String tag) {
            try {
                replace(id, (Fragment) replaceFragment.newInstance(), tag);
            } catch (IllegalAccessException | InstantiationException ignored) {
            }
            return this;
        }

        @Override
        public FragmentTransaction addToBackStack() {
            mAddBackStack = true;
            for (; mIndex < mOpl.size(); mIndex++) {
                mOpl.get(mIndex).isAddToBackStack = true;
            }
            return this;
        }

        @Override
        public int commit() {
            return commitInternal(true);
        }

        @Override
        public int commitNow() {
            if (mAddBackStack) {
                throw new IllegalStateException("Cannot addToBackStack");
            }
            return commitInternal(false);
        }

        @Override
        public int executePendingTransactions() {
            return commitInternal(false);
        }

        int commitInternal(boolean posted) {
            if (mCommitted) {
                throw new IllegalStateException("commit already called");
            }
            mCommitted = true;
            BackStackRecord bsr = new BackStackRecord(mOpl);
            if (!posted) {
                bsr.run();
            } else {
                mHandler.post(bsr::run);
            }
            return mAddBackStack ? mFragmentBackStack.allocBackStackIndex(bsr) : -1;
        }

    }

    public class FragmentManagerImpl implements FragmentManager {

        private final Fragment mParentFragment;

        public FragmentManagerImpl(Fragment fragment) {
            mParentFragment = fragment;
        }

        @NonNull
        @Override
        public final FragmentTransaction beginTransaction() {
            return mParentFragment == null ? new FragmentTransactionImpl() : new FragmentTransactionImpl(mParentFragment);
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
            return mFragmentBackStack.getBackStackSize();
        }

        @Override
        public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
            mFragmentBackStack.removeOnBackStackChangedListener(listener);
        }

        @Override
        public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
            mFragmentBackStack.addOnBackStackChangedListener(listener);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Fragment> T findFragmentByTag(String tag) {
            return (T) tagGetFragment(tag);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Fragment> T findFragmentById(int id) {
            View view = mActivity.findViewById(id);
            if (view instanceof FragmentView) {
                FragmentView fcv = (FragmentView) view;
                return (T) fcv.getFragment();
            }
            return null;
        }

    }
}
