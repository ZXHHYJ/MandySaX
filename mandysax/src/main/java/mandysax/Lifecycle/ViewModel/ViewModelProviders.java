package mandysax.Lifecycle.ViewModel;
import mandysax.Lifecycle.LifecycleAbstract;

public class ViewModelProviders
{
	public static ViewModelLifecycle of(LifecycleAbstract context)
	{	
		return new ViewModelLifecycle(context);
	}
}

