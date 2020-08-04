package mandysax.Lifecycle.ViewModel;

import mandysax.Lifecycle.LifecycleAbstract;
import mandysax.Lifecycle.LifecycleActivity;
import mandysax.Lifecycle.LifecycleEvent.LifecycleDestory;
import mandysax.Lifecycle.LifecycleEvent.LifecyclePause;
import mandysax.Lifecycle.LifecycleEvent.LifecycleViewModel;

public class ViewModelLifecycle
{
	private final LifecycleAbstract lifecycle;

	public ViewModelLifecycle(final LifecycleAbstract lifecycle)
	{
		this.lifecycle = lifecycle;
	}

	public <T extends ViewModel> T get(final Class<?> name)
	{
		for (int i=0;i < ViewModelManagement.ViewModelData.size();i++)
			if (ViewModelManagement.ViewModelData.get(i).getTag().equals(name.toString()))return (T)ViewModelManagement.ViewModelData.get(i);
		try
		{
			final Object load_class = Class.forName(name.getCanonicalName()).newInstance();
			lifecycle.getLifecycle().KillEvent(new LifecycleViewModel(){

					@Override
					public void onKill()
					{
						((ViewModel)load_class).onCleared();
					}		
				});
			return (T)load_class;
		}
		catch (IllegalAccessException e)
		{
		}
		catch (ClassNotFoundException e)
		{
		}
		catch (InstantiationException e)
		{
		}
		return null;
	}

}
