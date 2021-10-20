package mandysax.navigation;

import android.content.res.TypedArray;

import androidx.annotation.NonNull;

import mandysax.core.R;
import mandysax.core.app.OnBackPressedCallback;
import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentTransaction;
import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;
import mandysax.lifecycle.ViewModelProviders;
import mandysax.navigation.fragment.NavHostFragment;

/**
 * 导航控制器
 */
public final class NavController {

    private final NavHostFragment mNavFragment;

    private final NavControllerViewModel mViewModel;

    /**
     * @param navHostFragment 带导航的NavHostFragment
     */
    public NavController(@NonNull NavHostFragment navHostFragment) {
        mNavFragment = navHostFragment;
        mViewModel = ViewModelProviders.of(navHostFragment.getViewLifecycleOwner()).get(NavControllerViewModel.class);
    }

    /**
     * @param fragment 需要导航到的fragment
     */
    public synchronized <T extends Fragment> void navigate(T fragment) {
        mNavFragment.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {
            @Override
            public void Observer(Lifecycle.Event state) {
                if (state == Lifecycle.Event.ON_START) {
                    _navigate(fragment, 0, 0, 0, 0);
                    mNavFragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                }
            }
        });
    }

    public synchronized <T extends Fragment> void navigate(int animStyle, T fragment) {
        mNavFragment.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {
            @Override
            public void Observer(Lifecycle.Event state) {
                if (state == Lifecycle.Event.ON_START) {
                    int[] attr = new int[]{R.attr.fragmentEnterAnim, R.attr.fragmentExitAnim, R.attr.fragmentPopEnterAnim, R.attr.fragmentPopExitAnim};
                    TypedArray array = mNavFragment.getActivity().getTheme().obtainStyledAttributes(animStyle, attr);
                    _navigate(fragment, array.getResourceId(0, 0), array.getResourceId(1, 0), array.getResourceId(2, 0), array.getResourceId(3, 0));
                    array.recycle();
                    mNavFragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                }
            }
        });
    }

    private FragmentTransaction beginTransaction() {
        return mNavFragment.getFragmentPlusManager().beginTransaction();
    }

    private <T extends Fragment> void _navigate(T fragment, int fragmentEnterAnim, int fragmentExitAnim, int fragmentPopEnterAnim, int fragmentPopExitAnim) {
        FragmentTransaction fragmentTransaction = beginTransaction();
        fragmentTransaction.setCustomAnimations(fragmentEnterAnim, fragmentExitAnim, 0, 0);
        int navId = mNavFragment.getRoot().getId();
        Fragment nowFragment = mViewModel.getNowFragment();
        if (nowFragment != null)
            fragmentTransaction.hide(nowFragment);
        fragmentTransaction.add(navId, fragment);
        fragmentTransaction.commit();
        mViewModel.add(fragment);
        fragment.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {
            @Override
            public void Observer(Lifecycle.Event state) {
                if (state == Lifecycle.Event.ON_START) {
                    fragment.getActivity().getOnBackPressedDispatcher().addCallback(mNavFragment.getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                        @Override
                        public void handleOnBackPressed() {
                            if (mViewModel.getLastFragment() == null) {
                                remove();
                                fragment.getActivity().onBackPressed();
                                return;
                            }
                            FragmentTransaction fragmentTransaction = beginTransaction();
                            fragmentTransaction.setCustomAnimations(fragmentPopEnterAnim, 0,0,fragmentPopExitAnim);
                            fragmentTransaction.remove(fragment);
                            fragmentTransaction.show(mViewModel.getLastFragment());
                            fragmentTransaction.commit();
                            mViewModel.removeLast();
                            remove();
                        }
                    });
                }
                if (state == Lifecycle.Event.ON_DESTROY) {
                    fragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                }
            }
        });
    }

    /**
     * 后退一步
     */
    public synchronized void navigateUp() {
        mNavFragment.getActivity().onBackPressed();
    }

}
