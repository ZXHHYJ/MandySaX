package mandysax.lifecycle;

public final class ViewModelLifecycle
{
	private final Lifecycle lifecycle;

	private final Factory factory;

	public ViewModelLifecycle(final LifecycleOwner lifecycle)
	{
		this.lifecycle = lifecycle.getLifecycle();
		this.factory = null;
	}

	public ViewModelLifecycle(final LifecycleOwner lifecycle, Factory factory)
	{
		this.lifecycle = lifecycle.getLifecycle();
		this.factory = factory;
	}

	public <T extends ViewModel> T  get(final Class<T> name)
	{
		lifecycle.addObsever(new LifecycleObserver(){

				@Override
				public void Observer(Lifecycle.Event State)
				{
					if(State==Lifecycle.Event.ON_KILL)
					((ViewModel)DataEnum.VIEWMODEL.get(name.getCanonicalName())).onCleared();
				}
			
		});
		T viewmodel;
		return (viewmodel = (T)DataEnum.VIEWMODEL.get(name.getCanonicalName())) == null ?factory == null ? new ViewModelFactory().create(name): factory.create(name): viewmodel;
	}

	final class ViewModelFactory implements Factory
	{

		@Override
		public <T extends ViewModel> T create(Class<T> modelClass)
		{
			try
			{
				return modelClass.newInstance();
			}
			catch (Exception e)
			{
				return null;
			}
		}
	}

}
