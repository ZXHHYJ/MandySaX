package mandysax.lifecycle;
import android.util.Log;

public final class ViewModelLifecycle
{
	private final ViewModelStore mViewModelStore;

	private final Factory mFactory;

	public ViewModelLifecycle(final AppCompatActivity activity, final Factory factory)
	{
		this.mViewModelStore = activity.getViewModelStore();
		this.mFactory = factory;
	}

	public <T extends ViewModel> T get(final Class<T> name)
	{
        Log.d("get viewmodel",name.getName());
		T viewmodel=(viewmodel = (T)mViewModelStore.get(name)) == null ?mFactory == null ? new ViewModelFactory().create(name): mFactory.create(name): viewmodel;
		mViewModelStore.put(name,viewmodel);
		return viewmodel;
	}

	private final class ViewModelFactory implements Factory
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
