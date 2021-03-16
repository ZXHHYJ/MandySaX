package mandysax.plus.fragment;

public interface FragmentController2Impl
{
	public boolean popBackStack()

	public boolean popBackStack(int index)

	public int getBackStackEntryCount()

	public FragmentImpl getBackStackEntryAt(int index)
	
	public <T extends FragmentImpl> T findFragmentByTag(String tag)

	public <T extends FragmentImpl> T findFragmentById(int id)

	public FragmentController2Impl setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim)

	public FragmentController2Impl add(int id, FragmentImpl fragment)

	public FragmentController2Impl add(int id, FragmentImpl fragment, String tag)

	public FragmentController2Impl remove(FragmentImpl fragment)

	public FragmentController2Impl show(FragmentImpl fragment)

	public FragmentController2Impl hide(FragmentImpl fragment)

	public FragmentController2Impl replace(int id, Class replaceFragment)

	public FragmentController2Impl replace(int id, Class replaceFragment, String tag)

	public FragmentController2Impl addToBackStack()

	public FragmentController2Impl addOnBackStackChangedListener(FragmentController.OnBackStackChangedListener listener)

	public FragmentController2Impl removeOnBackStackChangedListener(FragmentController.OnBackStackChangedListener listener)

	public int commit()
}
