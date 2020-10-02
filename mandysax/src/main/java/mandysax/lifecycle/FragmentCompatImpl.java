package mandysax.lifecycle;
import mandysax.design.FragmentPage;

abstract interface FragmentCompatImpl
{
	public abstract void setFragmentPage(FragmentPage page);
	
	public abstract void startFragment(Class fragment);
}
