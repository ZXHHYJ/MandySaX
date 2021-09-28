package mandysax.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.livedata.MutableLiveData;

class FragmentStateManager extends FragmentStore {

    private final MutableLiveData<Boolean> mActivityCreated = new MutableLiveData<>();
    final Handler mHandler = new Handler(Looper.getMainLooper());
    FragmentActivity mActivity;
    Bundle mSavedInstanceState;

    void dumpSaveInstanceState(Bundle outState) {
        for (Fragment fragment : values()) {
            fragment.onSaveInstanceState(outState);
        }
    }

    void dumpOnMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        for (Fragment fragment : values()) {
            fragment.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        }
    }

    void dumpOnConfigurationChanged(Configuration newConfig) {
        for (Fragment fragment : values()) {
            fragment.onConfigurationChanged(newConfig);
        }
    }

    void dumpAdd(Fragment parentFragment, @NonNull Fragment fragment, int id, String tag) {
        tag = tag == null ? fragment.getClass().getCanonicalName() : tag;
        if (fragment.isAdded()) {
            throw new RuntimeException("Fragment " + fragment.getClass().getCanonicalName() + " has been added of ");
        } else {
            fragment.set(tag, id);
            fragment.dumpOnAttach(mActivity);
            fragment.dumpOnCreate(fragment.getArguments());
        }
        if (!fragment.isInLayout() && id != 0) {
            fragment.dispatchOnViewCreated(fragment.onCreateView(
                    mActivity.getLayoutInflater().cloneInContext(mActivity.getApplicationContext()),
                    fragment.getViewGroup(),
                    fragment.getArguments()
            ));
            mActivityCreated.lazy(p1 -> fragment.onActivityCreated(mSavedInstanceState));
        }
        if (parentFragment == null) {
            add(fragment);
        } else {
            add(parentFragment, fragment);
        }
    }

    void dumpShow(@NonNull Fragment fragment, int anim) {
        if (fragment.isDetached()) {
            throw new IllegalStateException("fragment not added");
        }
        if (fragment.getRoot() != null) {
            fragment.getRoot().setVisibility(View.VISIBLE);
            if (anim != 0) {
                fragment.getRoot().startAnimation(AnimationUtils.loadAnimation(fragment.getContext(), anim));
            }
            fragment.onHiddenChanged(false);
        }
    }

    void dumpRemove(Fragment fragment, int anim) {
        remove(fragment);
        if (fragment.getRoot() != null && anim != 0) {
            Animation exitAnim = AnimationUtils.loadAnimation(fragment.getContext(), anim);
            exitAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation p1) {
                }

                @Override
                public void onAnimationEnd(Animation p1) {
                    fragment.dispatchOnDestroyView();
                    fragment.dispatchOnDestroy();
                    fragment.dispatchOnDetach();
                }

                @Override
                public void onAnimationRepeat(Animation p1) {
                }
            });
            fragment.getRoot().startAnimation(exitAnim);
            return;
        }
        fragment.dispatchOnDestroyView();
        fragment.dispatchOnDestroy();
        fragment.dispatchOnDetach();
    }

    void dumpHide(@NonNull Fragment fragment, int anim) {
        if (fragment.isDetached()) {
            throw new IllegalStateException("fragment not added");
        }
        if (fragment.getRoot() != null) {
            if (anim != 0) {
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
            } else {
                fragment.getRoot().setVisibility(View.GONE);
            }
            fragment.onHiddenChanged(true);
        }

    }

    void dumpReplace(Op op) {
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
                    fragment.getLifecycle().addObserver(state -> {
                        if (state != Lifecycle.Event.ON_STOP) {
                            return;
                        }
                        dumpRemove(fragment, op.popExitAnim);
                    });
                } else {
                    if (op.removed == null) {
                        op.removed = new ArrayList<>();
                    }
                    op.removed.add(fragment);
                }
                dumpHide(fragment, op.exitAnim);
            }
        }
        dumpAdd(op.parentFragment, op.fragment, op.id, op.tag);
        dumpShow(op.fragment, op.popEnterAnim);
    }

    void dumpOnAttach(FragmentActivity activity) {
        mActivity = activity;
        for (Fragment fragment : values()) {
            fragment.dumpOnAttach(activity);
        }
    }

    void dumpResumeFragment() {
        for (Fragment fragment : values()) {
            if (fragment.isAdded() && fragment.isInLayout()) {
                if (fragment.getRoot() != null && fragment.getViewGroup() != null) {
                    fragment.getViewGroup().addView(fragment.getRoot());
                }
                if (fragment.isVisible()) {
                    dumpShow(fragment, 0);
                }
            }
            mActivityCreated.lazy(p1 -> fragment.onActivityCreated(mSavedInstanceState));
        }
    }

    void dumpOnStart() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnStart();
            }
        }
        if (mActivityCreated.getValue() == null) {
            mActivityCreated.setValue(true);
        }
    }

    void dumpOnRestart() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnRestart();
            }
        }
    }

    void dumpOnResume() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnResume();
            }
        }
    }

    void dumpOnPause() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnPause();
            }
        }
    }

    void dumpOnStop() {
        for (Fragment fragment : values()) {
            if (fragment.isVisible()) {
                fragment.dispatchOnStop();
            }
        }
    }

    void dumpOnDestroy() {
        for (Fragment fragment : values()) {
            fragment.dispatchOnDestroyView();
            fragment.dispatchOnDestroy();
            fragment.dispatchOnDetach();
        }
        mActivity = null;
        mSavedInstanceState = null;
        mActivityCreated.setValue(null);
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
