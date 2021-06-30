package mandysax.fragment;

import java.util.ArrayList;

public interface FragmentManagerImpl
{
	ArrayList<Fragment> getFragmentList();
	
	void clear();
	
	void addFragment(Fragment fragment);
	
	void removeFragment(Fragment fragment);

	Fragment tagGetFragment(String tag);
}
