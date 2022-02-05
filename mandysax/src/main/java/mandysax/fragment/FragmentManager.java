package mandysax.fragment;

/**
 * @author liuxiaoliu66
 */
public interface FragmentManager {
    FragmentTransaction beginTransaction();

    boolean popBackStack();

    boolean popBackStack(int index);

    int getBackStackEntryCount();

    /*Fragment getBackStackEntryAt(int index);*/

    void removeOnBackStackChangedListener(FragmentController.OnBackStackChangedListener listener);

    void addOnBackStackChangedListener(FragmentController.OnBackStackChangedListener listener);

    Fragment findFragmentByTag(String tag);

    Fragment findFragmentById(int id);
}

