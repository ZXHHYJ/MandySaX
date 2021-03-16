package mandysax.plus.lifecycle;
import java.util.HashMap;

public final class ViewModelStore implements ViewModelStoreImpl
{
	private HashMap<Class,ViewModel> mMap;

    @Override
	public void put(Class key, ViewModel viewModel)
	{
		if (mMap == null)
		{
			synchronized (this)
			{
				if (mMap == null)
					mMap = new HashMap<Class,ViewModel>();
			}
		}
	    mMap.put(key, viewModel);
    }

    @Override
    public ViewModel get(Class key)
	{
		if (mMap == null)return null;
        return mMap.get(key);
    }

    /**
     *  Clears internal storage and notifies ViewModels that they are no longer used.
     */
    @Override
    public void clear()
	{
		if (mMap != null)
		{
			for (ViewModel vm : mMap.values())
			{
				vm.onCleared();
			}
			mMap.clear();
		}
    }
}
