package mandysax.plus.lifecycle;

public interface ViewModelStoreImpl
{
    public void put(Class key, ViewModel viewModel)
    
    public ViewModel get(Class key)
    
    public void clear()
}
