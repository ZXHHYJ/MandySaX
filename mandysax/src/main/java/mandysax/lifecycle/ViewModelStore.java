package mandysax.lifecycle;

public interface ViewModelStore {

    void put(Class<?> key, ViewModel viewModel);

    ViewModel get(Class<?> key);

    void clear();
}
