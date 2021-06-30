package mandysax.navigation.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import mandysax.R;
import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentActivity;
import mandysax.fragment.FragmentView;
import mandysax.navigation.fragment.NavController;
import mandysax.navigation.impl.NavigationImpl;

public class Navigation extends FragmentView implements NavigationImpl {

    public Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (size() != 0) return;
        @SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Navigation);
        String name = typedArray.getString(R.styleable.Navigation_android_fragment);
        typedArray.recycle();
        if (name != null) {
            try {
                Fragment fragment = (Fragment) Class.forName(name).newInstance();
                addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        startFragment(fragment);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {

                    }
                });
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public NavController getFragment() {
        NavController navController = (NavController) super.getFragment();
        if (navController.getActivity() == null)
            navController.set(getId(), getActivity());
        return navController;
    }

    @Override
    public String getName() {
        return NavController.class.getCanonicalName();
    }

    @Override
    public void startFragment(Fragment openFragment) {
        getFragment().startFragment(openFragment);
    }

    public static void startFragment(FragmentActivity fragmentActivity, int navId, Fragment openFragment) {
        Navigation navigation = fragmentActivity.findViewById(navId);
        assert navigation != null;
        navigation.startFragment(openFragment);
    }

    public static void onBackPressed(FragmentActivity fragmentActivity, int navId) {
        Navigation navigation = fragmentActivity.findViewById(navId);
        assert navigation != null;
        navigation.onBackPressed();
    }

    public static void startFragment(Fragment addedFragment, int navId, Fragment openFragment) {
        startFragment(addedFragment.getActivity(), navId, openFragment);
    }

    public static void onBackPressed(Fragment fragment, int navId) {
        onBackPressed(fragment.getActivity(), navId);
    }

    @Override
    public void setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim) {
        getFragment().setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
    }

    @Override
    public int size() {
        return getFragment().size();
    }

    @Override
    public boolean onBackPressed() {
        return getFragment().onBackPressed();
    }

}
