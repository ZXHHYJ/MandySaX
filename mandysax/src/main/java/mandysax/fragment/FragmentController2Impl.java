package mandysax.fragment;

public interface FragmentController2Impl
{
	boolean popBackStack();

	boolean popBackStack(int index);

	int getBackStackEntryCount();

	Fragment getBackStackEntryAt(int index);
	
	<T extends Fragment> T findFragmentByTag(String tag);

	<T extends Fragment> T findFragmentById(int id);

	FragmentController2Impl setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim);

	FragmentController2Impl add(int id, Fragment fragment);

	FragmentController2Impl add(int id, Fragment fragment, String tag);

	FragmentController2Impl remove(Fragment fragment);

	FragmentController2Impl show(Fragment fragment);

	FragmentController2Impl hide(Fragment fragment);

	FragmentController2Impl replace(int id, Fragment replaceFragment);

	FragmentController2Impl replace(int id, Fragment replaceFragment, String tag);
	
	FragmentController2Impl replace(int id, Class replaceFragment);

	FragmentController2Impl replace(int id, Class replaceFragment, String tag);

	FragmentController2Impl addToBackStack();

	FragmentController2Impl addOnBackStackChangedListener(FragmentControllerImpl.OnBackStackChangedListener listener);

	FragmentController2Impl removeOnBackStackChangedListener(FragmentControllerImpl.OnBackStackChangedListener listener);

	FragmentController2Impl getFragmentPlusManager(Fragment fragment);
    
	int commit();
	
	int commitNow();
}
