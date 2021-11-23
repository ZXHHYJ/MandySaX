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

    <T extends Fragment> T findFragmentByTag(String tag);

    <T extends Fragment> T findFragmentById(int id);
}

