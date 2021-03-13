package mandysax.plus.fragment;

import java.util.Set;
import java.util.Map;

public interface FragmentManagerImpl
{
	public void addFragment(Fragment fragment, String tag)

	public void removeFragment(Fragment fragment)

	public Fragment tagGetFragment(String tag)
	
	public Set<Map.Entry<String, Fragment>> entrySet()
	
	public void clear()
}
