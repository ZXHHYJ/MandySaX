package mandysax.plus.fragment;
import java.util.ArrayList;
import java.util.List;

public final class FragmentManager implements FragmentManagerImpl
{

	private static class HaveFragment
	{

		public HaveFragment(Fragment fragment, String tag)
		{
			this.fragment = fragment;
			this.tag = tag;
		}

		Fragment fragment;
		String tag;

		@Override
		public boolean equals(Object obj)
		{
			return tag.equals(obj);
		}

	}

    private final List<HaveFragment> fragments=new ArrayList<HaveFragment>();

	@Override
	public void addFragment(Fragment fragment, String tag)
	{
		fragments.add(new HaveFragment(fragment, tag));
	}

	@Override
	public void removeFragment(Fragment fragment)
	{
		fragments.remove(getRemoveData(fragment));
	}

	@Override
	public List<Fragment> getFragments()
	{
		ArrayList<Fragment> fragments=new ArrayList<Fragment>();
		for (HaveFragment have:this.fragments)
		{
			fragments.add(have.fragment);
		}
		return fragments;
	}

	private HaveFragment getRemoveData(Fragment fragment)
	{
		for (HaveFragment have:fragments)
		{
			if (have.fragment.equals(fragment))
				return have;
		}
		return null;
	}

	@Override
	public Fragment tagGetFragment(String tag)
	{
		for (HaveFragment have:fragments)
		{
			if (have.equals(tag))
				return have.fragment;
		}
		return null;
    }

}
