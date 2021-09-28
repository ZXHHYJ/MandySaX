package mandysax.lifecycle;

public interface ViewModelStoreImpl {

    void put(Class key, ViewModel viewModel);

    ViewModel get(Class key);

    void clear();
}
