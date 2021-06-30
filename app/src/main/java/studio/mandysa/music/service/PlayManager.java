package studio.mandysa.music.service;
import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentActivity;

public final class PlayManager
{

	public static final String ID="weixiaoliu";

	final PlayManagerFragment fragment;

    private PlayManager(FragmentActivity activity)
    {
		PlayManagerFragment fragment1 = activity.getFragmentPlusManager().findFragmentByTag(ID);
		if (fragment1 == null)
			activity.getFragmentPlusManager().add(0, fragment1 = new PlayManagerFragment(), ID).commitNow();
		fragment = fragment1;
	}

	public static PlayManagerImpl getInstance(FragmentActivity activity)
	{	
        if (activity == null)return null;
      	return new PlayManager(activity).fragment;
	}

	public static PlayManagerImpl getInstance(Fragment fragment)
	{
        if (fragment == null)return null;
        return getInstance(fragment.getActivity());
    }

}
