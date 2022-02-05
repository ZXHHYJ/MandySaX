package mandysax.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import mandysax.navigation.fragment.NavHostFragment;

/**
 * @author ZXHHYJ
 */
@SuppressLint("ViewConstructor")
public final class NavHostFragmentView extends FrameLayout {

    private final NavHostFragment mFragment;

    public NavHostFragmentView(@NonNull Context context, NavHostFragment navHostFragment) {
        super(context);
        mFragment = navHostFragment;
    }

    @NonNull
    public NavHostFragment getNavHostFragment() {
        return mFragment;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
}
