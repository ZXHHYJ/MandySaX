package mandysax.plus.fragment;

public interface FragmentController2Impl
{
	public Fragment findFragmentByTag(String tag)

	public FragmentController2Impl setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim)
	
	public FragmentController2Impl add(int id, Fragment fragment)
	
	public FragmentController2Impl add(int id, Fragment fragment, String tag)
	
	public FragmentController2Impl remove(Fragment fragment)

	public FragmentController2Impl show(Fragment fragment)
	
	public FragmentController2Impl hide(Fragment fragment)

	public FragmentController2Impl replace(int id, Class replaceFragment)

	public FragmentController2Impl replace(int id, Class replaceFragment, String tag)

	public FragmentController2Impl addToBackStack()
	
	public FragmentController2Impl addOnBackStackChangedListener(FragmentController.OnBackStackChangedListener listener)
	
	public FragmentController2Impl removeOnBackStackChangedListener(FragmentController.OnBackStackChangedListener listener)
	
	public int commit()
}
