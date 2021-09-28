package mandysax.fragment;

import java.util.ArrayList;

/**
 * @author liuxiaoliu66
 */
final class Op {
    int id;
    String tag;
    FragmentController.STACK stack;
    Fragment parentFragment;
    Fragment fragment;
    int enterAnim;
    int exitAnim;
    int popEnterAnim;
    int popExitAnim;
    boolean isAddToBackStack;
    ArrayList<Fragment> removed;
}
