package mandysax.lifecycle;
import mandysax.lifecycle.*;

public final class ViewModelProviders
{
	public static ViewModelLifecycle of(LifecycleOwner lifecycle)
	{	
		return new ViewModelLifecycle(lifecycle);
	}
	
	public static ViewModelLifecycle of(LifecycleOwner lifecycle,Factory factory)
	{	
		return new ViewModelLifecycle(lifecycle,factory);
	}
}

