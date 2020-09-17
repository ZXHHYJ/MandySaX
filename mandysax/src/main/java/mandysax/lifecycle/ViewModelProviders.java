package mandysax.lifecycle;
import mandysax.lifecycle.*;

public final class ViewModelProviders
{
	public static ViewModelLifecycle of(LifecycleAbstract context)
	{	
		return new ViewModelLifecycle(context);
	}
	
	public static ViewModelLifecycle of(LifecycleAbstract context,Factory factory)
	{	
		return new ViewModelLifecycle(context,factory);
	}
}

