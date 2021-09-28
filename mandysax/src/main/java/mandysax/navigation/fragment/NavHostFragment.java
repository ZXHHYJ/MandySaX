package mandysax.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentTransaction;
import mandysax.fragment.FragmentView;
import mandysax.navigation.NavController;

public class NavHostFragment extends Fragment {

    private FrameLayout mRoot;

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = new FrameLayout(inflater.getContext());
        mRoot.setId(View.generateViewId());
        return mRoot;
    }

    @Override
    protected void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        for (int i = 0; i < mRoot.getChildCount(); i++) {
            if (mRoot.getChildAt(i) instanceof FragmentView)
                mRoot.getChildAt(i).setVisibility(hidden ? View.GONE : View.VISIBLE);
        }
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
