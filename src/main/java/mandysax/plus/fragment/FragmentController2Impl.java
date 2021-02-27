package mandysax.plus.fragment;
import mandysax.plus.fragment.FragmentController.*;

public interface FragmentController2Impl
{
	public Fragment findFragmentByTag(String tag)

	public FragmentController2 setCustomAnimations(int startAnim, int exitAnim , int startAnim2,int exitAnim2)
	
	public FragmentController2 add(int id, Fragment fragment)
	
	public FragmentController2 add(int id, Fragment fragment, String tag)
	
	public FragmentController2 remove(Fragment fragment)

	public FragmentController2 show(Fragment fragment)
	
	public FragmentController2 show()
	
	public FragmentController2 hide(Fragment fragment)

	public FragmentController2 replace(int id, Class replaceFragment)

	public FragmentController2 replace(int id, Class replaceFragment, String tag)

	public FragmentController2 addToBackStack()
	
	public void commit()
}
