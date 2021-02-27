package mandysax.plus.fragment;
import java.util.List;

public interface FragmentManagerImpl
{
	public void addFragment(Fragment fragment, String tag)

	public void removeFragment(Fragment fragment)

	public List<Fragment> getFragments()

	public Fragment tagGetFragment(String tag)
}
