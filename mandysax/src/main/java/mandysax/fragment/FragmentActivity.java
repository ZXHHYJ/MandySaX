package mandysax.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

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

    private FragmentHost mHost;

    @NonNull
    @Contract(" -> new")
    public final FragmentManager getFragmentPlusManager() {
        return getFragmentController().getFragmentManager(null);
    }

    private FragmentStateManager getFragmentStateManager() {
        return getFragmentController();
    }

    private FragmentHost getFragmentHost() {
        return mHost == null ? mHost = ViewModelProviders.of(this).get(FragmentHost.class) : mHost;
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
    public void setContentView(View view) {
        super.setContentView(view);
        getFragmentStateManager().dispatchResumeFragment();
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        getFragmentStateManager().dispatchResumeFragment();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        getFragmentStateManager().dispatchResumeFragment();
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

