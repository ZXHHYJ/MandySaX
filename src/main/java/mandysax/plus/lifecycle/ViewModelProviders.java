package mandysax.plus.lifecycle;

import mandysax.plus.fragment.Fragment;
import mandysax.plus.fragment.FragmentActivity;

public final class ViewModelProviders
{
	public static ViewModelLifecycle of(FragmentActivity activity)
	{	
		return new ViewModelLifecycle(activity, null);
	}

	public static ViewModelLifecycle of(FragmentActivity activity, Factory factory)
	{	
		return new ViewModelLifecycle(activity, factory);
	}

	public static ViewModelLifecycle of(Fragment fragment)
	{	
		return new ViewModelLifecycle(fragment.getActivity(), null);
	}

	public static ViewModelLifecycle of(Fragment fragment, Factory factory)
	{	
		return new ViewModelLifecycle(fragment.getActivity(), factory);
	}
	
	public static final class ViewModelLifecycle
	{
		private static ViewModelStore mViewModelStore;

		private static ViewModelProviders.Factory mFactory;

		public ViewModelLifecycle(FragmentActivity activity, ViewModelProviders.Factory factory)
		{
			this.mViewModelStore = activity.getViewModelStore();
			this.mFactory = factory;
		}

		public static <T extends ViewModel> T get(Class<T> name)
		{
			T viewmodel=(viewmodel = (T)mViewModelStore.get(name)) == null ?mFactory == null ? new ViewModelProviders.Factory().create(name): mFactory.create(name): viewmodel;
			mViewModelStore.put(name, viewmodel);
			mViewModelStore=null;
			mFactory=null;
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

