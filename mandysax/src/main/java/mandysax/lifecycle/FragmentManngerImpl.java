package mandysax.lifecycle;

public interface FragmentManngerImpl
{
	public FragmentMannger add(int id, Fragment fragment);
	
	public FragmentMannger add(int id, Fragment fragment, Object tag);
	
	public FragmentMannger remove(Fragment fragment);
	
	public FragmentMannger show(Fragment fragment);
	
	public FragmentMannger hide(Fragment fragment);
	
	public FragmentMannger replace(int id, Class replaceFragment);
	
	public FragmentMannger addToBackStack();
	
	public Fragment findFragmentByTag(Object tag);
	
	//public FragmentActivity getActivity();
}
