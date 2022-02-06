package mandysax.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;

class FragmentStateManager extends FragmentStore {

    final Handler mHandler = new Handler(Looper.getMainLooper());
    FragmentActivity mActivity;
    Bundle mSavedInstanceState;

    void dispatchSaveInstanceState(Bundle outState) {
        for (Fragment fragment : values()) {
            fragment.onSaveInstanceState(outState);
        }
    }

    void dispatchOnMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        for (Fragment fragment : values()) {
            fragment.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        }
    }

    void dispatchOnConfigurationChanged(Configuration newConfig) {
        for (Fragment fragment : values()) {
            fragment.onConfigurationChanged(newConfig);
        }
    }

    /**
     * 添加fragment
     *
     * @param parentFragment 父fragment
     * @param fragment       待添加的fragment
     * @param id             view id
     * @param tag            标记
     */
    void dispatchAdd(Fragment parentFragment, @NonNull Fragment fragment, int id, String tag) {
        if (fragment.isAdded())
            throw new RuntimeException(fragment + " has been added");
        if (tag == null) {
            tag = fragment.getClass().getSimpleName();
        }
        boolean repeat = findFragmentByTag(tag) != null;
        StringBuilder tagBuilder = new StringBuilder(tag);
        while (repeat) {
            repeat = findFragmentByTag(tagBuilder.append("+").toString()) != null;
        }
        tag = tagBuilder.toString();
        fragment.initialize(tag, id);
        fragment.dispatchOnAttach(mActivity);
        fragment.dispatchOnCreate(fragment.getArguments());
        if (parentFragment == null) {
            add(fragment);
        } else {
            add(parentFragment, fragment);
        }
        if (!fragment.isInLayout() && id != 0) {
            fragment.dispatchOnViewCreated(fragment.onCreateView(
                    mActivity.getLayoutInflater().cloneInContext(mActivity.getApplicationContext()),
                    fragment.getViewContainer(),
                    fragment.getArguments()
            ));
        }
        mActivity.getLifecycle().addObserver(new LifecycleObserver() {
            @Override
            public void observer(Lifecycle.Event state) {
                switch (state) {
                    case ON_RESTART:
                    case ON_START:
                    case ON_RESUME:
                        fragment.onActivityCreated(mSavedInstanceState);
                        mActivity.getLifecycle().removeObserver(this);
                }
            }
        });
        fragment.dispatchAdd();
    }

    /**
     * 显示fragment
     *
     * @param fragment 待显示的fragment
     * @param anim     动画id
     */
    void dispatchShow(@NonNull Fragment fragment, int anim) {
        if (fragment.isDetached()) {
            throw new IllegalStateException("fragment not added");
        }
        if (fragment.getRoot() == null) {
            return;
        }
        fragment.getRoot().setVisibility(View.VISIBLE);
        if (anim != 0) {
            fragment.getRoot().startAnimation(AnimationUtils.loadAnimation(fragment.getContext(), anim));
        }
        fragment.onHiddenChanged(false);
    }

    /**
     * 移除fragment
     *
     * @param fragment 待移除的fragment
     * @param anim     动画id
     */
    void dispatchRemove(@NonNull Fragment fragment, int anim) {
        if (fragment.isInLayout() && anim != 0) {
            Animation exitAnim = AnimationUtils.loadAnimation(fragment.getContext(), anim);
            exitAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation p1) {
                }

                @Override
                public void onAnimationEnd(Animation p1) {
                    fragment.dispatchOnDestroyView();
                    fragment.dispatchOnDetach();
                    remove(fragment);
                }

                @Override
                public void onAnimationRepeat(Animation p1) {
                }
            });
            fragment.getRoot().startAnimation(exitAnim);
            return;
        }
        fragment.dispatchOnDestroyView();
        fragment.dispatchOnDetach();
        remove(fragment);
    }

    /**
     * 隐藏fragment
     *
     * @param fragment 待隐藏的fragment
     * @param anim     动画id
     */
    void dispatchHide(@NonNull Fragment fragment, int anim) {
        if (fragment.isDetached()) {
            throw new IllegalStateException("fragment not added");
        }
        if (!fragment.isInLayout()) {
            return;
        }
        if (anim == 0) {
            fragment.getRoot().setVisibility(View.GONE);
        } else {
            Animation exitAnim = AnimationUtils.loadAnimation(fragment.getContext(), anim);
            exitAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation p1) {
                }

                @Override
                public void onAnimationEnd(Animation p1) {
                    fragment.getRoot().setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation p1) {
                }
            });
            fragment.getRoot().startAnimation(exitAnim);
        }
        fragment.onHiddenChanged(true);
    }

    /**
     * 切换fragment
     *
     * @param op 操作集合
     */
    void dispatchReplace(Op op) {
        for (Fragment fragment : values()) {
            if (fragment.getRoot() == null) {
                continue;
            }
            if (fragment.getRoot().getParent() == null) {
                continue;
            }
            if (((View) fragment.getRoot().getParent()).getId() == op.id) {
                if (!fragment.isVisible()) {
                    continue;
                }
                if (!op.isAddToBackStack) {
                    fragment.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {
                        @Override
                        public void observer(Lifecycle.Event state) {
                            if (state != Lifecycle.Event.ON_STOP) {
                                return;
                            }
                            dispatchRemove(fragment, op.popExitAnim);
                            fragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                        }
                    });
                } else {
                    if (op.removed == null) {
                        op.removed = new ArrayList<>();
                    }
                    op.removed.add(fragment);
                }
                dispatchHide(fragment, op.exitAnim);
            }
        }
        dispatchAdd(op.parentFragment, op.fragment, op.id, op.tag);
        dispatchShow(op.fragment, op.popEnterAnim);
    }

    void dispatchOnAttach(FragmentActivity activity) {
        mActivity = activity;
        for (Fragment fragment : values()) {
            fragment.dispatchOnAttach(activity);
        }
    }

    void dispatchResumeFragment() {
        for (Fragment fragment : values())
            if (fragment.isInLayout()) {
                Fragment parentFragment = fragment.getParentFragment();
                if (parentFragment != null)
                    if (fragment.getRoot() != null && parentFragment.getRoot() != null) {
                        ViewGroup parent = parentFragment.findViewById(fragment.getLayoutId());
                        if (parent != null) {
                            parent.addView(fragment.getRoot());
                            continue;
                        }
                    }

                if (fragment.getRoot() != null && fragment.getViewContainer() != null) {
                    fragment.getViewContainer().addView(fragment.getRoot());
                }
            }
    }

    void dispatchOnStart() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnStart();
                fragment.onActivityCreated(mSavedInstanceState);
            }
        }
    }

    void dispatchOnRestart() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnRestart();
            }
        }
    }

    void dispatchOnResume() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnResume();
            }
        }
    }

    void dispatchOnPause() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnPause();
            }
        }
    }

    void dispatchOnStop() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnStop();
            }
        }
    }

    void dispatchOnDestroy() {
        for (Fragment fragment : values()) {
            fragment.dispatchOnDestroyView();
            fragment.dispatchOnDestroy();
            fragment.dispatchOnDetach();
        }
        mSavedInstanceState = null;
        mActivity = null;
    }

    @Override
    public void addFragment(Fragment fragment) {
        super.addFragment(fragment);
        fragment.dispatchAdd();
    }

    @Override
    public void removeFragment(Fragment fragment) {
        super.removeFragment(fragment);
        fragment.dispatchRemove();
    }

}
