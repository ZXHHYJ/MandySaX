package mandysax.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;
import mandysax.lifecycle.LifecycleRegistry;

/**
 * @author liuxiaoliu66
 */
public class Fragment extends FragmentLifecycle implements FragmentImpl, LifecycleOwner {

    public static final String TAG = "fragment";

    private Lifecycle mLifecycle;
    private final FragmentViewLifecycleOwner mViewLifecycleOwner = new FragmentViewLifecycleOwner();
    private boolean mDetached;
    private int mLayoutId = 0;
    private boolean mAdded;
    private boolean mRemoving;
    private String mTag;
    private View mView;
    private Bundle mArguments;
    private FragmentActivity mActivity;

    /**
     * @return activity的fragmentPlusManager
     */
    @Override
    @NonNull
    public final FragmentManager getFragmentManager() {
        return requireActivity().getFragmentPlusManager();
    }

    /**
     * @return fragment的fragmentPlusManager
     */
    @Override
    @NonNull
    public final FragmentManager getChildFragmentManager() {
        return requireActivity().getFragmentController().getFragmentManager(this);
    }

    @Nullable
    @Contract(pure = true)
    @Override
    public final FragmentManager getParentFragmentManager() {
        return null;
    }

    /**
     * @return 返回LayoutInflater(不会使Activity内存泄漏)
     */
    @NonNull
    public
    final LayoutInflater getLayoutInflater() {
        return requireActivity().getLayoutInflater().cloneInContext(mActivity.getApplicationContext());
    }

    @Override
    public <T extends View> T findViewById(int i) {
        return mView != null ? mView.findViewById(i) : null;
    }

    /**
     * @return activity的Lifecycle
     */
    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    /**
     * @return 获取fragment的ViewLifecycleOwner
     * 此lifecycle才是fragment的生命周期
     * viewModels也由它管理
     */
    @NonNull
    public FragmentViewLifecycleOwner getViewLifecycleOwner() {
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
        switch (mLifecycle.event) {
            case ON_START:
            case ON_RESUME:
            case ON_RESTART:
                return true;
        }
        return false;
    }

    @Override
    public boolean isVisible() {
        return mView != null && mView.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean isHidden() {
        return !isVisible();
    }

    void initialize(String tag, int id) {
        mTag = tag;
        mLayoutId = id;
    }

    void dispatchOnAttach(Context context) {
        mActivity = (FragmentActivity) context;
        mLifecycle = mActivity.getLifecycle();
        mDetached = false;
        mRemoving = false;
        onAttach(context);
    }

    void dispatchOnCreate(Bundle bundle) {
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

    ViewGroup getViewContainer() {
        ViewGroup viewGroup = requireActivity().findViewById(mLayoutId);
        if (viewGroup == null) {
            Log.w(TAG, this + " parent is null");
        }
        return viewGroup;
    }

    int getLayoutId() {
        return mLayoutId;
    }

    void dispatchOnViewCreated(View view) {
        if (view == null) {
            return;
        }
        mView = view;
        getViewContainer().addView(view);
        onViewCreated(view, mArguments);
    }

    void dispatchOnStart() {
        onStart();
    }

    void dispatchOnRestart() {
        onRestart();
    }

    void dispatchOnResume() {
        onResume();
    }

    void dispatchOnPause() {
        onPause();
    }

    void dispatchOnStop() {
        onStop();
    }

    void dispatchOnDestroyView() {
        mRemoving = true;
        if (mView != null && mView.getParent() != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
        onDestroyView();
    }

    void dispatchOnDestroy() {
        onDestroy();
    }

    void dispatchOnDetach() {
        mActivity = null;
        mLifecycle = null;
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