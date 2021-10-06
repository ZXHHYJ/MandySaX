package mandysax.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentTransaction;
import mandysax.navigation.NavController;
import mandysax.navigation.NavHostFragmentView;

public class NavHostFragment extends Fragment {

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NavHostFragmentView mRoot = new NavHostFragmentView(inflater.getContext(), this);
        mRoot.setId(View.generateViewId());
        return mRoot;
    }

    @Override
    protected void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public NavController getNavController() {
        return getNavController(null);
    }

    public NavController getNavController(FragmentTransaction fragmentTransaction) {
        return new NavController(this, fragmentTransaction);
    }

    @NonNull
    public static NavHostFragment create(Fragment fragment) {
        NavHostFragment navHostFragment = new NavHostFragment();
        navHostFragment.getNavController().navigate(fragment);
        return navHostFragment;
    }

}
