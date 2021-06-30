package mandysax.navigation.fragment;

import java.util.ArrayList;

import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentActivity;
import mandysax.fragment.FragmentController2Impl;
import mandysax.navigation.impl.NavigationImpl;

public class NavController extends Fragment implements NavigationImpl {

    private int mId;

    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    private FragmentActivity mActivity;

    public void set(int id, FragmentActivity fragmentActivity) {
        mId = id;
        mActivity = fragmentActivity;
    }

    @Override
    public FragmentActivity getActivity() {
        return mActivity;
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public boolean onBackPressed() {
        if (mFragments.size() >= 2) {
            getFragmentPlusManager().hide(mFragments.get(mFragments.size() - 1)).show(mFragments.get(mFragments.size() - 2)).commitNow();
            mFragments.remove(mFragments.size() - 1);
            return true;
        }
        return false;
    }

    @Override
    public void startFragment(Fragment openFragment) {
        if (openFragment.isAdded())
            throw new RuntimeException("Fragment has been added");
        FragmentController2Impl fragmentController2 = getFragmentPlusManager();
        if (!mFragments.isEmpty()) {
            fragmentController2.hide(mFragments.get(mFragments.size() - 1));
        }
        fragmentController2.add(mId, openFragment);
        mFragments.add(openFragment);
        fragmentController2.commitNow();
    }

    int enterAnim, exitAnim, popEnterAnim, popExitAnim;

    @Override
    public FragmentController2Impl getFragmentPlusManager() {
        return getActivity().getFragmentPlusManager().setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    @Override
    public void setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        this.popEnterAnim = popEnterAnim;
        this.popExitAnim = popExitAnim;
    }

    @Override
    public int size() {
        return mFragments.size();
    }

}
