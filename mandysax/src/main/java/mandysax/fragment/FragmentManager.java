package mandysax.fragment;
import java.util.ArrayList;

public final class FragmentManager implements FragmentManagerImpl
{

	@Override
	public ArrayList<Fragment> getFragmentList()
	{
		return fragments;
	}

	private final ArrayList<Fragment> fragments=new ArrayList<>();

	@Override
	public void clear()
	{	
		fragments.clear();
	}

	@Override
	public void addFragment(Fragment fragment)
	{
		for (Fragment f:fragments)
			if (f.getTag().equals(fragment.getTag()))
				throw new RuntimeException("Duplicate tag");
		fragments.add(fragment);
	}

	@Override
	public void removeFragment(Fragment fragment)
	{
		fragments.remove(fragment);
	}

	@Override
	public Fragment tagGetFragment(String tag)
	{
		for (Fragment f:fragments)
			if (f.getTag().equals(tag))
				return f;
		return null;
    }

}
