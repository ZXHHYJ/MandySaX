package mandysax.plus.lifecycle;
import mandysax.plus.fragment.Fragment;
import mandysax.plus.fragment.FragmentActivity;

public final class ViewModelProviders
{
	public static ViewModelLifecycle of(FragmentActivity activity)
	{	
		return new ViewModelLifecycle(activity.getViewModelStore(), null);
	}

	public static ViewModelLifecycle of(FragmentActivity activity, Factory factory)
	{	
		return new ViewModelLifecycle(activity.getViewModelStore(), factory);
	}

	public static ViewModelLifecycle of(Fragment fragment)
	{	
		return new ViewModelLifecycle(fragment.getActivity().getViewModelStore(), null);
	}

	public static ViewModelLifecycle of(Fragment fragment, Factory factory)
	{	
		return new ViewModelLifecycle(fragment.getActivity().getViewModelStore(), factory);
	}

	/*
	 *2.0.0中新增API
	 *用途是提供一个全局ViewModel
	 */
    public static ViewModelLifecycle of(ViewModelStore viewModelStore, Factory factory)
	{	
		return new ViewModelLifecycle(viewModelStore, factory);
	}

	/*
	 *21.2.25
	 */
	public static final class ViewModelLifecycle
	{
		private final ViewModelStore mViewModelStore;

		private final ViewModelProviders.Factory mFactory;

		/*
		 *在2.0.0中ViewModelLifecycle不再需要FragmentActivity参数
		 *只需要提供ViewModelStore,管理器便可工作
		 */
		public ViewModelLifecycle(ViewModelStore viewModelStore, ViewModelProviders.Factory factory)
		{
			this.mViewModelStore = viewModelStore;
			this.mFactory = factory;
		}

		public <T extends ViewModel> T get(Class<T> name)
		{
			T viewmodel=(viewmodel = (T)mViewModelStore.get(name)) == null ?mFactory == null ? new ViewModelProviders.Factory().create(name): mFactory.create(name): viewmodel;
			mViewModelStore.put(name, viewmodel);
			return viewmodel;
		}

	}

	public static class Factory
	{

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

