package mandysax.navigation.impl;

import mandysax.fragment.Fragment;

public interface NavigationImpl {

    void startFragment(Fragment openFragment);

    void setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim);

    int size();

    boolean onBackPressed();

}
