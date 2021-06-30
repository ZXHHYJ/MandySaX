package mandysax.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleImpl;
import mandysax.lifecycle.LifecycleOwner;
import mandysax.lifecycle.ViewModelStoreImpl;
import mandysax.lifecycle.ViewModelStoreOwner;

public class Fragment implements FragmentImpl, LifecycleOwner, ViewModelStoreOwner {
    private boolean mDetached;
    private FragmentController mFci;
    private int mLayoutId = 0;
    private final Lifecycle mLifecycle = new Lifecycle();
    private boolean mRemoving;
    private boolean mResumed;
    private String mTag;
    private View mView;

    /**
     * @param bundle FragmentActivity bundle
     */
    protected void onActivityCreated(Bundle bundle) {
    }

    /**
     * @param context FragmentActivity
     */
    protected void onAttach(Context context) {
    }

    protected void onConfigurationChanged(Configuration configuration) {
    }

    protected void onCreate(Bundle bundle) {
    }

    protected void onDestroy() {
    }

    protected void onDestroyView() {
    }

    protected void onDetach() {
    }

    protected void onHiddenChanged(boolean z) {
    }

    @Deprecated
    protected void onMultiWindowModeChanged(boolean z) {
    }

    protected void onMultiWindowModeChanged(boolean z, Configuration configuration) {
    }

    protected void onPause() {
    }

    protected void onRestart() {
    }

    protected void onResume() {
    }

    protected void onSaveInstanceState(Bundle bundle) {
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    protected void onViewCreated(View view, Bundle bundle) {
    }

    @Override
    public FragmentController2Impl getFragmentPlusManager() {
        return mFci.getActivity().getFragmentPlusManager().getFragmentPlusManager(this);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    protected LayoutInflater getLayoutInflater() {
        return mFci.getActivity().getLayoutInflater();
    }

    @Override
    public <T extends View> T findViewById(int i) {
        return mView != null ? mView.findViewById(i) : null;
    }

    @Override
    public LifecycleImpl getLifecycle() {
        return mLifecycle;
    }

    @Override
    public ViewModelStoreImpl getViewModelStore() {
        return mFci.getActivity().getViewModelStore();
    }

    /*
     *fix bug form 2.2.0
     */
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
        if (mDetached || mFci == null) {
            return null;
        }
        return mFci.getActivity();
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, Bundle bundle) {
        getActivity().startActivity(intent, bundle);
    }

    /**
     * @return this Fragment isAdded
     */
    @Override
    public boolean isAdded() {
        return getActivity() != null && getActivity().getFragmentPlusManager().findFragmentByTag(this.mTag) != null;
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

    void set(FragmentController fragmentController, String str, int i) {
        mFci = fragmentController;
        mTag = str;
        mLayoutId = i;
    }

    void _onAttach(Context context) {
        mDetached = false;
        mRemoving = false;
        onAttach(context);
    }

    void _onCreate(Bundle bundle) {
        mLifecycle.dispatchOnCreate();
        onCreate(bundle);
    }

    protected int onCreateView() {
        return 0;
    }

    protected View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }

    void _onViewCreated(View view, Bundle bundle) {
        if (view == null)
            return;
        if (getViewGroup() == null) throw new NullPointerException("Can't find parent layout");
        mView = view;
        //view.setVisibility(View.GONE); 2.1.0中添加fragment时默认执行show
        getViewGroup().addView(view);
        onViewCreated(view, bundle);
    }

    /**
     * @return this Fragment root layout
     */
    @Override
    public View getRoot() {
        return mView;
    }

    ViewGroup getViewGroup() {
        if (getActivity() == null)
            return null;
        return getActivity().findViewById(mLayoutId);
    }

    void _onStart() {
        mResumed = true;
        mLifecycle.dispatchOnStart();
        onStart();
    }

    void _onRestart() {
        mLifecycle.dispatchOnRestart();
        onRestart();
    }

    void _onResume() {
        mLifecycle.dispatchOnResume();
        onResume();
    }

    void _onPause() {
        mLifecycle.dispatchOnPause();
        onPause();
    }

    void _onStop() {
        mResumed = false;
        mLifecycle.dispatchOnStop();
        onStop();
    }

    void _onDestroyView() {
        this.mRemoving = true;
        if (!(mView == null || mView.getParent() == null))
            ((ViewGroup) mView.getParent()).removeView(mView);
        onDestroyView();
    }

    void _onDestroy() {
        mLifecycle.dispatchOnDestroy();
        onDestroy();
    }

    void _onDetach() {
        mDetached = true;
        onDetach();
    }
}