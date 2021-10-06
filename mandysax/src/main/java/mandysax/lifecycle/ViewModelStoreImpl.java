package mandysax.lifecycle;

import java.util.HashMap;

/**
 * @author liuxiaoliu66
 */
public class ViewModelStoreImpl implements ViewModelStore {
    private HashMap<Class<?>, ViewModel> mMap;

    /**
     * @param key       Extends the ViewModel class
     * @param viewModel put in ViewModelStore ViewModel object
     */
    @Override
    public void put(Class<?> key, ViewModel viewModel) {
        if (mMap == null) {
            synchronized (ViewModelStoreImpl.class) {
                if (mMap == null) {
                    mMap = new HashMap<>(16);
                }
            }
        }
        mMap.put(key, viewModel);
    }

    /**
     * @param key Extends the ViewModel class
     * @return Your ViewModel object
     */
    @Override
    public ViewModel get(Class<?> key) {
        if (mMap == null) {
            return null;
        }
        ViewModel viewModel = mMap.get(key);
        if (viewModel != null) {
            if (key.isInstance(viewModel)) {
                return viewModel;
            }
        }
        return null;
    }


    /**
     * clear ViewModelStore
     */
    @Override
    public void clear() {
        if (mMap != null) {
            for (ViewModel vm : mMap.values()) {
                vm.onCleared();
            }
            mMap.clear();
            mMap = null;
        }
    }

}
