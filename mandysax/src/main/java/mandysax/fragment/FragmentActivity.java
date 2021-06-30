package mandysax.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import mandysax.app.LifecycleActivity;
import mandysax.lifecycle.LifecycleOwner;
import mandysax.lifecycle.ViewModelProviders;
import mandysax.lifecycle.ViewModelStore;
import mandysax.lifecycle.ViewModelStoreImpl;
import mandysax.lifecycle.ViewModelStoreOwner;

public class FragmentActivity extends LifecycleActivity implements LifecycleOwner, ViewModelStoreOwner {

    //记一个不可以忘记的朋友<晓柳>

    private ViewModelStore mViewModelStore;

    private NonConfigurationInstances mLastNonConfigurationInstances;

    public final FragmentController2Impl getFragmentPlusManager() {
        return getFragmentController().getFragmentController2();
    }

    private FragmentControllerImpl getFragmentController() {
        return ViewModelProviders.of(this, new ViewModelProviders.Factory() {
            @Override
            public FragmentController create(Class modelClass) {
                return new FragmentController(FragmentActivity.this);
            }
        }).get(FragmentController.class);
    }

    @Override
    public ViewModelStoreImpl getViewModelStore() {
        if (mViewModelStore == null) {
            getLastNonConfigurationInstance();
            if (mViewModelStore == null) mViewModelStore = new ViewModelStore();
            return mViewModelStore;
        }
        return mViewModelStore;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        if (mLastNonConfigurationInstances == null) {
            NonConfigurationInstances nci = new NonConfigurationInstances();
            nci.viewModelStore = mViewModelStore == null ? new ViewModelStore() : mViewModelStore;
            return nci;
        } else return mLastNonConfigurationInstances;
    }

    @Override
    public Object getLastNonConfigurationInstance() {
        NonConfigurationInstances nci = (FragmentActivity.NonConfigurationInstances) super.getLastNonConfigurationInstance();
        if (nci == null) return null;
        if (mViewModelStore == null) mViewModelStore = nci.viewModelStore;
        return nci;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentController().saveInstanceState(outState);
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        getFragmentController().multiWindowModeChanged(isInMultiWindowMode, newConfig);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getFragmentController().configurationChanged(newConfig);
    }

	/*
	 恢复fragment
	 */

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getFragmentController().resumeFragment();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        getFragmentController().resumeFragment();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        getFragmentController().resumeFragment();
    }

	/*
	 恢复fragment
	 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory2(new FragmentLayoutInflaterFactory());//代理
        super.onCreate(savedInstanceState);
        getFragmentController().create(this, savedInstanceState);//配置变更后context已经被管理者移除了，需要重新赋值
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            getViewModelStore().clear();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentController().onBackFragment())
            return;
        if (!getFragmentController().getFragmentController2().popBackStack()) {
            super.onBackPressed();
        }
    }

    private static final class NonConfigurationInstances {
        ViewModelStore viewModelStore;//ViewModel的管理者
    }

}

