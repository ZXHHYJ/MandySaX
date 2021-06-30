package mandysax.fragment;

import android.content.res.Configuration;
import android.os.Bundle;

public interface FragmentControllerImpl {

    interface OnBackStackChangedListener {
        void onBackStackChanged();
    }

    boolean onBackFragment();

    FragmentController2Impl getFragmentController2();

    void resumeFragment();

    void create(FragmentActivity activity, Bundle activitySavedInstanceState);

    void saveInstanceState(Bundle outState);

    void multiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig);

    void configurationChanged(Configuration newConfig);

}
