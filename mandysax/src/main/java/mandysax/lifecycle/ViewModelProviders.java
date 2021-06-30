package mandysax.lifecycle;

public final class ViewModelProviders
{

	/**
	 * @param owner Object that implements ViewModelStoreOwner
	 * @return ViewModelLifecycle object
	 */
	public static ViewModelLifecycle of(ViewModelStoreOwner owner)
	{	
		return new ViewModelLifecycle(owner.getViewModelStore(), null);
	}

	/**
	 * @param owner Object that implements ViewModelStoreOwner
	 * @param factory ViewModelProviders.Factory object
	 * @return ViewModelLifecycle object
	 */
	public static ViewModelLifecycle of(ViewModelStoreOwner owner, Factory factory)
	{	
		return new ViewModelLifecycle(owner.getViewModelStore(), factory);
	}

	/**
	 * @param viewModelStore Object that implements ViewModelStoreImpl
	 * @param factory ViewModelProviders.Factory object
	 * @return ViewModelLifecycle object
	 */
	/*
	 *2.0.0中新增API
	 *用途是提供一个全局ViewModel
	 */
    public static ViewModelLifecycle of(ViewModelStoreImpl viewModelStore, Factory factory)
	{	
		return new ViewModelLifecycle(viewModelStore, factory);
	}

	/*
	 *21.2.25
	 *21.5.18
	 */
	public static final class ViewModelLifecycle
	{
		private final ViewModelStoreImpl mViewModelStore;

		private final ViewModelProviders.Factory mFactory;

		/*
		 *在2.0.0中ViewModelLifecycle不再需要FragmentActivity参数
		 *只需要提供ViewModelStore,管理器便可工作
		 */
		public ViewModelLifecycle(ViewModelStoreImpl viewModelStore, ViewModelProviders.Factory factory)
		{
			this.mViewModelStore = viewModelStore;
			this.mFactory = factory;
		}

		/**
		 * @param name Object that extends ViewModel
		 * @param <T> extends ViewModel
		 * @return Your ViewModel object
		 */
		public <T extends ViewModel> T get(Class<T> name)
		{
			T viewModel=(T)mViewModelStore.get(name);
			if (viewModel == null)
			{
				viewModel = mFactory == null ? new ViewModelProviders.Factory(){}.create(name): mFactory.create(name);
				mViewModelStore.put(name, viewModel);
				return viewModel;
			}
			else 
			if (name.isInstance(viewModel))
			{
				return viewModel;
			}
			else return null;
		}

	}

	public interface Factory
	{

		/**
		 * @param modelClass The ViewModel class that needs to be create
		 * @param <T> extends ViewModel
		 * @return Your ViewModel object
		 */
		default <T extends ViewModel> T create(Class<T> modelClass)
		{
			try
			{
				return modelClass.newInstance();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}
}

