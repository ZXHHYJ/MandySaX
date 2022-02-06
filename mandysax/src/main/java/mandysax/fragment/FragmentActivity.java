package mandysax.fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import mandysax.core.app.ComponentActivity;
import mandysax.core.view.LayoutInflaterCompat;
import mandysax.lifecycle.ViewModelProviders;

/**
 * @author liuxiaoliu66
 */
public class FragmentActivity extends ComponentActivity {

    //记一个不可以忘记的朋友<晓柳>

    private FragmentManagerViewModel mHost;

    private boolean mResumed = false;

    @NonNull
    @Contract(" -> new")
    public final FragmentManager getFragmentPlusManager() {
        return getFragmentController().getFragmentManager(null);
    }

    private FragmentStateManager getFragmentStateManager() {
        return getFragmentController();
    }

    private FragmentManagerViewModel getFragmentHost() {
        return mHost == null ? mHost = ViewModelProviders.of(this).get(FragmentManagerViewModel.class) : mHost;
    }

    FragmentController getFragmentController() {
        return getFragmentHost().mController;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentStateManager().dispatchSaveInstanceState(outState);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        getFragmentStateManager().dispatchOnMultiWindowModeChanged(isInMultiWindowMode, newConfig);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getFragmentStateManager().dispatchOnConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(super.getLayoutInflater(), new FragmentLayoutInflaterFactory());
        super.onCreate(savedInstanceState);
        getFragmentStateManager().dispatchOnAttach(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFragmentStateManager().dispatchOnResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getFragmentStateManager().dispatchOnStart();
        if (!mResumed) {
            getFragmentStateManager().dispatchResumeFragment();
            mResumed = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFragmentStateManager().dispatchOnRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getFragmentStateManager().dispatchOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getFragmentStateManager().dispatchOnStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getFragmentStateManager().dispatchOnDestroy();
        if (!isChangingConfigurations()) {
            getViewModelStore().clear();
        }
    }

}

