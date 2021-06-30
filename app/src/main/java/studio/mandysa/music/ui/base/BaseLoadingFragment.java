package studio.mandysa.music.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import studio.mandysa.music.R;

public class BaseLoadingFragment extends BaseFragment {

    private ProgressBar mProgressBar;

    private ViewStub mViewStub;

    private View mView;

    private ViewGroup mViewGroup;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private void setShowAnimation(@NonNull View view) {
        AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(300);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(mShowAnimation);
    }

    private void setHideAnimation(@NonNull View view) {
        AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(300);
        view.startAnimation(mHideAnimation);
        view.setVisibility(View.GONE);
    }

    private void initView() {
        if (mView.getParent() == null) {
            getViewGroup().addView(mView);
        }
    }

    public void hide() {
        mViewStub.setVisibility(View.GONE);
        mHandler.postDelayed(() -> {
            initView();
            setHideAnimation(mProgressBar);
            setShowAnimation(mView);
        }, 150);
    }

    public void show() {
        initView();
        mProgressBar.setVisibility(View.GONE);
        mView.setVisibility(View.GONE);
        mViewStub.setVisibility(View.VISIBLE);
    }

    public void onRefresh() {
        setHideAnimation(mViewStub);
        setShowAnimation(mProgressBar);
        mView.setVisibility(View.GONE);
    }

    public <T extends View> T findViewById(int i) {
        return mView.findViewById(i);
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.base_loading, viewGroup, false);
    }

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewStub = view.findViewById(R.id.vs_baseLoading_view);
        mViewStub.setOnInflateListener((p1, p2) -> p2.setOnClickListener(p11 -> onRefresh()));
        mProgressBar = view.findViewById(R.id.proBar_baseLoading_view);
        mView = getLayoutInflater().inflate(onCreateView(), getViewGroup(), false);
        mView.setVisibility(View.GONE);
    }

    public ViewGroup getViewGroup() {
        return mViewGroup == null ? mViewGroup = (ViewGroup) getRoot() : mViewGroup;
    }

}
