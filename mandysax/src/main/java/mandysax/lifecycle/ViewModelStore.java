package mandysax.lifecycle;

/**
 * @author ZXHHYJ
 */
public interface ViewModelStore {

    void put(Class<?> key, ViewModel viewModel);

    ViewModel get(Class<?> key);

    void clear();
}
