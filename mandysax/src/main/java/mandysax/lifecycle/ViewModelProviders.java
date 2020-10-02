package mandysax.lifecycle;

public final class ViewModelProviders
{
	public static ViewModelLifecycle of(AppCompatActivity activity)
	{	
		return new ViewModelLifecycle(activity, null);
	}

	public static ViewModelLifecycle of(AppCompatActivity activity, Factory factory)
	{	
		return new ViewModelLifecycle(activity, factory);
	}

	public static ViewModelLifecycle of(FragmentCompat fragment)
	{	
		return new ViewModelLifecycle((AppCompatActivity)fragment.getActivity(), null);
	}

	public static ViewModelLifecycle of(FragmentCompat fragment, Factory factory)
	{	
		return new ViewModelLifecycle((AppCompatActivity)fragment.getActivity(), factory);
	}
}

