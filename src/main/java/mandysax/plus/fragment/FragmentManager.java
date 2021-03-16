package mandysax.plus.fragment;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class FragmentManager implements FragmentManagerImpl
{

	@Override
	public Set<Map.Entry<String, Fragment>> entrySet()
	{
		return fragments.entrySet();
	}

	private final LinkedHashMap<String,Fragment> fragments=new LinkedHashMap<>();

    @Override
	public void clear()
	{	
		fragments.clear();
	}

	@Override
	public void addFragment(Fragment fragment, String tag)
	{
		fragments.put(tag, fragment);
	}

	@Override
	public void removeFragment(Fragment fragment)
	{
		for (Map.Entry<String, Fragment> entry : entrySet())
		{
			if (entry.getValue().equals(fragment))
			{
				fragments.remove(entry.getKey());
				return;
			}
		}
	}

	@Override
	public Fragment tagGetFragment(String tag)
	{
		return fragments.get(tag);
    }

}
