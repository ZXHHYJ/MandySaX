package mandysax.lifecycle;
import mandysax.lifecycle.*;

public final class ViewModelProviders
{
	public static ViewModelLifecycle of(LifecycleOwner context)
	{	
		return new ViewModelLifecycle(context);
	}
	
	public static ViewModelLifecycle of(LifecycleOwner context,Factory factory)
	{	
		return new ViewModelLifecycle(context,factory);
	}
}

