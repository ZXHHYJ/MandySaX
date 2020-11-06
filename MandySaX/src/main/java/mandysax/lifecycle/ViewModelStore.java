package mandysax.lifecycle;
import java.util.HashMap;
import android.util.Log;

public final class ViewModelStore
{
	private final HashMap<Class,ViewModel> mMap = new HashMap<Class,ViewModel>();

	final void put(Class key, ViewModel viewModel)
	{
        ViewModel oldViewModel = mMap.put(key, viewModel);
        if (oldViewModel != null)
		{
            oldViewModel.onCleared();
        }
    }

    final ViewModel get(Class key)
	{
        return mMap.get(key);
    }

    /**
     *  Clears internal storage and notifies ViewModels that they are no longer used.
     */
    public final void clear()
	{
        //Log.d("clear","clear all viewmodel");
        for (ViewModel vm : mMap.values())
		{
            vm.onCleared();
        }
        mMap.clear();
    }
}
