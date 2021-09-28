package mandysax.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;

/**
 * @author liuxiaoliu66
 */
public class Fragment extends FragmentLifecycle implements FragmentImpl, LifecycleOwner {
    private final Lifecycle mLifecycle = new Lifecycle();
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

    @Override
    public @NonNull
    FragmentManager getFragmentPlusManager() {
        return getActivity().getFragmentController().getFragmentManager(this);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public @NonNull
    final LayoutInflater getLayoutInflater() {
        return getActivity().getLayoutInflater().cloneInContext(mActivity.getApplicationContext());
    }

    @Override
    public <T extends View> T findViewById(int i) {
        return mView != null ? mView.findViewById(i) : null;
    }

    @Override
    public @NonNull
    Lifecycle getLifecycle() {
        return mLifecycle;
    }

    public @NonNull
    FragmentViewLifecycleOwner getViewLifecycleOwner() {
        return mViewLifecycleOwner;
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public String getTag() {
        return mTag;
    }

    @Override
    public FragmentActivity getActivity() {
        if (mActivity == null) {
            throw new NullPointerException("Fragment does not hold activity");
        }
        return mActivity;
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, Bundle bundle) {
        getActivity().startActivity(intent, bundle);
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
        mLifecycle.dispatchOnCreate();
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
        ViewGroup viewGroup = getActivity().findViewById(mLayoutId);
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
        mLifecycle.dispatchOnStart();
        onStart();
    }

    void dispatchOnRestart() {
        mLifecycle.dispatchOnRestart();
        onRestart();
    }

    void dispatchOnResume() {
        mLifecycle.dispatchOnResume();
        onResume();
    }

    void dispatchOnPause() {
        mLifecycle.dispatchOnPause();
        onPause();
    }

    void dispatchOnStop() {
        mResumed = false;
        mLifecycle.dispatchOnStop();
        onStop();
    }

    void dispatchOnDestroyView() {
        this.mRemoving = true;
        if (!(mView == null || mView.getParent() == null)) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
        onDestroyView();
    }

    void dispatchOnDestroy() {
        mLifecycle.dispatchOnDestroy();
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
        if (hidden) {
            mViewLifecycleOwner.getLifecycle().dispatchOnStop();
        } else {
            mViewLifecycleOwner.getLifecycle().dispatchOnStart();
        }
    }

    void dispatchAdd() {
        mAdded = true;
        mViewLifecycleOwner.getLifecycle().dispatchOnCreate();
    }

    void dispatchRemove() {
        mAdded = false;
        mViewLifecycleOwner.getLifecycle().dispatchOnDestroy();
    }

}