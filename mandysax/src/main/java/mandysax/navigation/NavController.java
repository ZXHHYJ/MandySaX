package mandysax.navigation;

import static mandysax.lifecycle.Lifecycle.Event.ON_DESTROY;

import android.content.res.TypedArray;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import mandysax.core.R;
import mandysax.core.app.OnBackPressedCallback;
import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentTransaction;
import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;
import mandysax.lifecycle.LifecycleRegistry;
import mandysax.lifecycle.ViewModelProviders;
import mandysax.navigation.fragment.NavHostFragment;

/**
 * 导航控制器
 *
 * @author ZXHHYJ
 */
public final class NavController {

    private final NavHostFragment mNavHost;

    private final NavControllerViewModel mViewModel;

    /**
     * @param navHostFragment 带导航的NavHostFragment
     */
    public NavController(@NonNull NavHostFragment navHostFragment) {
        mNavHost = navHostFragment;
        mViewModel = ViewModelProviders.of(navHostFragment.getViewLifecycleOwner()).get(NavControllerViewModel.class);
    }

    /**
     * @param fragment 需要导航到的fragment
     */
    @MainThread
    public <T extends Fragment> void navigate(T fragment) {
        new NavFragmentOnStartCallback(mNavHost, fragment);
    }

    @MainThread
    public <T extends Fragment> void navigate(int animResId, T fragment) {
        new NavFragmentOnStartCallback(mNavHost, fragment, animResId);
    }

    private FragmentTransaction beginTransaction() {
        return mNavHost.getChildFragmentManager().beginTransaction();
    }

    private <T extends Fragment> void navigate(T fragment, int fragmentEnterAnim, int fragmentExitAnim, int fragmentPopEnterAnim, int fragmentPopExitAnim) {
        FragmentTransaction fragmentTransaction = beginTransaction();
        fragmentTransaction.setCustomAnimations(fragmentEnterAnim, fragmentExitAnim, 0, 0);
        int navId = mNavHost.getRoot().getId();
        Fragment nowFragment = mViewModel.getNowFragment();
        if (nowFragment != null) {
            fragmentTransaction.hide(nowFragment);
        }
        fragmentTransaction.add(navId, fragment);
        fragmentTransaction.commit();
        mViewModel.add(fragment);
        fragment.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {

            private boolean mAddBackCallback = false;

            @Override
            public void observer(@NonNull Lifecycle.Event state) {
                if (state == Lifecycle.Event.ON_CREATE) {
                    mNavHost.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {

                        {
                            fragment.getViewLifecycleOwner().getLifecycle().addObserver(state1 -> {
                                if (state1 == ON_DESTROY) {
                                    mNavHost.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                                }
                            });
                        }

                        @Override
                        public void observer(@NonNull Lifecycle.Event state) {
                            switch (state) {
                                case ON_START:
                                    ((LifecycleRegistry) fragment.getViewLifecycleOwner().getLifecycle()).markState(Lifecycle.Event.ON_START);
                                    break;
                                case ON_STOP:
                                    ((LifecycleRegistry) fragment.getViewLifecycleOwner().getLifecycle()).markState(Lifecycle.Event.ON_STOP);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                }
                if (state == Lifecycle.Event.ON_START && !mAddBackCallback) {
                    mAddBackCallback = true;
                    fragment.requireActivity().getOnBackPressedDispatcher().addCallback(mNavHost.getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            if (mViewModel.getLastFragment() == null) {
                                //已经到达顶层
                                //销毁监听
                                remove();
                                navigateUp();
                                return;
                            }
                            FragmentTransaction fragmentTransaction = beginTransaction();
                            fragmentTransaction.setCustomAnimations(fragmentPopEnterAnim, 0, 0, fragmentPopExitAnim);
                            fragmentTransaction.remove(fragment);
                            fragmentTransaction.show(mViewModel.getLastFragment());
                            fragmentTransaction.commit();
                            mViewModel.removeLast();
                            //销毁当前监听
                            remove();
                        }
                    });
                }
                if (state == ON_DESTROY) {
                    fragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                }
            }
        });
    }

    /**
     * run Activity.onBackPressed()
     */
    public void navigateUp() {
        mNavHost.requireActivity().onBackPressed();
    }

    private class NavFragmentOnStartCallback implements LifecycleObserver {

        private final NavHostFragment mNavHostFragment;

        private final Fragment mNavFragment;

        private final int mAnimResId;

        NavFragmentOnStartCallback(NavHostFragment navHostFragment, Fragment navFragment) {
            mNavHostFragment = navHostFragment;
            mNavFragment = navFragment;
            mAnimResId = 0;
            mNavHostFragment.getViewLifecycleOwner().getLifecycle().addObserver(this);
        }

        NavFragmentOnStartCallback(NavHostFragment navHostFragment, Fragment navFragment, int animResId) {
            mNavHostFragment = navHostFragment;
            mNavFragment = navFragment;
            mAnimResId = animResId;
            mNavHostFragment.getViewLifecycleOwner().getLifecycle().addObserver(this);
        }

        @Override
        public void observer(@NonNull Lifecycle.Event state) {
            if (state == Lifecycle.Event.ON_START) {
                if (mAnimResId != 0) {
                    int[] attr = new int[]{R.attr.fragmentEnterAnim, R.attr.fragmentExitAnim, R.attr.fragmentPopEnterAnim, R.attr.fragmentPopExitAnim};
                    TypedArray array = mNavHost.requireActivity().getTheme().obtainStyledAttributes(mAnimResId, attr);
                    navigate(mNavFragment, array.getResourceId(0, 0), array.getResourceId(1, 0), array.getResourceId(2, 0), array.getResourceId(3, 0));
                    array.recycle();
                } else
                    navigate(mNavFragment, 0, 0, 0, 0);
                mNavHostFragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
            }
        }
    }

}
