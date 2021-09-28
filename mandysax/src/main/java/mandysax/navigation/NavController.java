package mandysax.navigation;

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

    private final FragmentTransaction mFragmentTransaction;

    /**
     * @param navHostFragment     需要导航的HostFragment
     * @param fragmentTransaction 自定义事务
     */
    public NavController(NavHostFragment navHostFragment, FragmentTransaction fragmentTransaction) {
        mNavFragment = navHostFragment;
        mViewModel = ViewModelProviders.of(navHostFragment.getViewLifecycleOwner()).get(NavControllerViewModel.class);
        mFragmentTransaction = fragmentTransaction;
    }

    /**
     * @param fragment 需要导航到的fragment
     * @param <T>      碎片泛型
     */
    public synchronized <T extends Fragment> void navigate(T fragment) {
        mNavFragment.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {
            @Override
            public void Observer(Lifecycle.Event state) {
                if (state == Lifecycle.Event.ON_START) {
                    _navigate(fragment, mFragmentTransaction == null ? mNavFragment.getFragmentPlusManager().beginTransaction() : mFragmentTransaction);
                    mNavFragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                }
            }
        });
    }

    private <T extends Fragment> void _navigate(T fragment, FragmentTransaction fragmentTransaction) {
        int navId = mNavFragment.getRoot().getId();
        final Fragment lastFragment = mViewModel.getLastFragment();
        if (lastFragment != null) {
            fragmentTransaction
                    .hide(lastFragment)
                    .add(navId, fragment)
                    .addToBackStack();
            fragment.getViewLifecycleOwner().getLifecycle().addObserver(state -> {
                if (state == Lifecycle.Event.ON_STOP && mViewModel.getLastFragment() == fragment) {
                    mViewModel.removeLast();
                }
            });
        } else {
            fragmentTransaction
                    .add(navId, fragment);
        }
        mViewModel.add(fragment, fragmentTransaction.commit());
    }

    /**
     * 后退一步
     */
    public synchronized void navigateUp() {
        if (mViewModel.getBackStackIndexLast() >= 2)
            popBackStack();
    }

    private void popBackStack() {
        if (mViewModel.getBackStackEntryCount() >= 1) {
            mNavFragment
                    .getFragmentPlusManager()
                    .popBackStack(
                            mViewModel.getBackStackIndexLast()
                    );
        }
    }

}
