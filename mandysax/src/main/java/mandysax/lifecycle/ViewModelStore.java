package mandysax.lifecycle;
import java.util.HashMap;

//此处疏忽了，忘记去掉final修饰符 21.5.20
//fix bug form v2.1.0
public /*final*/ class ViewModelStore implements ViewModelStoreImpl
{
	private HashMap<Class,ViewModel> mMap;

    /**
     * @param key Extends the ViewModel class
     * @param viewModel put in ViewModelStore ViewModel object
     */
    @Override
    public void put(Class key, ViewModel viewModel) {
        if (mMap == null)
            mMap = new HashMap<>();
        mMap.put(key, viewModel);
    }

    /**
     * @param key Extends the ViewModel class
     * @return Your ViewModel object
     */
    @Override
    public ViewModel get(Class key) {
        if (mMap == null)return null;
        ViewModel viewModel=mMap.get(key);
        if (viewModel != null)
            if (key.isInstance(viewModel))
                return viewModel;
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
        }

    }
}
