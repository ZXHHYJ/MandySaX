package mandysax.fragment;

/**
 * @author liuxiaoliu66
 */
@SuppressWarnings("All")
public interface FragmentTransaction {

    FragmentTransaction setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim);

    FragmentTransaction add(int id, Fragment fragment);

    FragmentTransaction add(int id, Fragment fragment, String tag);

    FragmentTransaction remove(Fragment fragment);

    FragmentTransaction show(Fragment fragment);

    FragmentTransaction hide(Fragment fragment);

    FragmentTransaction replace(int id, Fragment replaceFragment);

    FragmentTransaction replace(int id, Fragment replaceFragment, String tag);

    FragmentTransaction replace(int id, Class<? super Fragment> replaceFragment);

    FragmentTransaction replace(int id, Class<? super Fragment> replaceFragment, String tag);

    FragmentTransaction addToBackStack();

    int commit();

    int commitNow();

    int executePendingTransactions();

    /*int commitAllowingStateLoss();*/
}
