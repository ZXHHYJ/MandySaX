package mandysax.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;
import mandysax.lifecycle.LifecycleRegistry;

/**
 * @author liuxiaoliu66
 */
public class Fragment extends FragmentLifecycle implements FragmentImpl, LifecycleOwner {
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry();
    private final FragmentViewLifecycleOwner mViewLifecycleOwner = new FragmentViewLifecycleOwner();
    private boolean mDetached;
    private int mLayoutId = 0;
    private boolean mAdded;
    private boolean mRemoving;
    private boolean mResumed;
    private String mTag;
    private View mView;
    private Bundle mArguments;
    private FragmentActivity mActivity;

    /**
     * @return 当前fragment的子fragmentPlusManager
     */
    @Override
    public @NonNull
    FragmentManager getFragmentPlusManager() {
        return requireActivity().getFragmentController().getFragmentManager(this);
    }

    /**
     * @return 返回不影响activity生命周期的LayoutInflater
     */
    public @NonNull
    final LayoutInflater getLayoutInflater() {
        return requireActivity().getLayoutInflater().cloneInContext(mActivity.getApplicationContext());
    }

    @Override
    public <T extends View> T findViewById(int i) {
        return mView != null ? mView.findViewById(i) : null;
    }

    /**
     * @return 此lifecycle与activity的lifecycle同步
     */
    @Override
    public @NonNull
    Lifecycle getLifecycle() {
        return mLifecycle;
    }

    /**
     * @return 获取fragment的ViewLifecycleOwner
     * 此lifecycle才是fragment的生命周期
     * viewModels也由它管理
     */
    public @NonNull
    FragmentViewLifecycleOwner getViewLifecycleOwner() {
        return mViewLifecycleOwner;
    }

    /**
     * @return ApplicationContext
     */
    @Override
    public Context getContext() {
        return requireActivity().getApplicationContext();
    }

    /**
     * @return fragment添加时设置的tag
     */
    @Override
    public String getTag() {
        return mTag;
    }

    /**
     * @return 获取宿主activity
     */
    @Override
    @Nullable
    public FragmentActivity getActivity() {
        return mActivity;
    }

    @Override
    @NonNull
    public FragmentActivity requireActivity() {
        if (mActivity == null) {
            throw new NullPointerException("Fragment does not hold activity");
        }
        return mActivity;
    }

    @Override
    public void startActivity(Intent intent) {
        requireActivity().startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, Bundle bundle) {
        requireActivity().startActivity(intent, bundle);
    }

    @Override
    public boolean isAdded() {
        return mAdded;
    }

    @Override
    public boolean isDetached() {
        return mDetached;
    }

    @Override
    public boolean isRemoving() {
        return mRemoving;
    }

    @Override
    public boolean isInLayout() {
        return mView != null;
    }

    @Override
    public boolean isResumed() {
        return mResumed;
    }

    @Override
    public boolean isVisible() {
        return mView != null && mView.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean isHidden() {
        return !isVisible();
    }

    void set(String tag, int id) {
        mTag = tag;
        mLayoutId = id;
    }

    void dumpOnAttach(Context context) {
        mActivity = (FragmentActivity) context;
        mDetached = false;
        mRemoving = false;
        onAttach(context);
    }

    void dumpOnCreate(Bundle bundle) {
        mLifecycle.markState(Lifecycle.Event.ON_CREATE);
        onCreate(bundle);
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public void setArguments(Bundle args) {
        if (isAdded()) {
            throw new IllegalStateException("Fragment already added and state has been saved");
        }
        mArguments = args;
    }

    @Override
    public View getRoot() {
        return mView;
    }

    ViewGroup getViewGroup() {
        ViewGroup viewGroup = requireActivity().findViewById(mLayoutId);
        if (viewGroup == null) {
            throw new IllegalStateException("Can't find parent layout or " + getClass().getCanonicalName());
        }
        return viewGroup;
    }

    void dispatchOnViewCreated(View view) {
        if (view == null) {
            return;
        }
        mView = view;
        getViewGroup().addView(view);
        onViewCreated(view, mArguments);
    }

    void dispatchOnStart() {
        mResumed = true;
        mLifecycle.markState(Lifecycle.Event.ON_START);
        onStart();
    }

    void dispatchOnRestart() {
        mLifecycle.markState(Lifecycle.Event.ON_RESTART);
        onRestart();
    }

    void dispatchOnResume() {
        mLifecycle.markState(Lifecycle.Event.ON_RESUME);
        onResume();
    }

    void dispatchOnPause() {
        mLifecycle.markState(Lifecycle.Event.ON_PAUSE);
        onPause();
    }

    void dispatchOnStop() {
        mResumed = false;
        mLifecycle.markState(Lifecycle.Event.ON_STOP);
        onStop();
    }

    void dispatchOnDestroyView() {
        mRemoving = true;
        if (mView != null && mView.getParent() != null)
            ((ViewGroup) mView.getParent()).removeView(mView);
        onDestroyView();
    }

    void dispatchOnDestroy() {
        mLifecycle.markState(Lifecycle.Event.ON_DESTROY);
        onDestroy();
    }

    void dispatchOnDetach() {
        mActivity = null;
        mDetached = true;
        onDetach();
    }

    @Override
    protected void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getLifecycleRegistry().markState(hidden ? Lifecycle.Event.ON_STOP : Lifecycle.Event.ON_START);
    }

    void dispatchAdd() {
        mAdded = true;
        getLifecycleRegistry().markState(Lifecycle.Event.ON_CREATE);
    }

    void dispatchRemove() {
        mAdded = false;
        getLifecycleRegistry().markState(Lifecycle.Event.ON_DESTROY);
    }

    private LifecycleRegistry getLifecycleRegistry() {
        return (LifecycleRegistry) getViewLifecycleOwner().getLifecycle();
    }

}