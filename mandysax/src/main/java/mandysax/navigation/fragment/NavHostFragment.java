package mandysax.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import mandysax.fragment.Fragment;
import mandysax.navigation.NavController;
import mandysax.navigation.NavHostFragmentView;

/**
 * @author ZXHHYJ
 */
public class NavHostFragment extends Fragment {

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NavHostFragmentView mRoot = new NavHostFragmentView(inflater.getContext(), this);
        mRoot.setId(View.generateViewId());
        return mRoot;
    }

    public NavController getNavController() {
        return new NavController(this);
    }

    @NonNull
    public static NavHostFragment create(Fragment fragment) {
        NavHostFragment navHostFragment = new NavHostFragment();
        navHostFragment.getNavController().navigate(fragment);
        return navHostFragment;
    }

}
