package mandysax.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mandysax.core.R;
import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;
import mandysax.lifecycle.ViewModelProviders;

/**
 * @author liuxiaoliu66
 */
public final class FragmentView extends FrameLayout implements LifecycleObserver {

    private String mTag = null;
    private Fragment mFragment;

    @SuppressLint("CustomViewStyleable")
    public FragmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Fragment);
        String name = typedArray.getString(R.styleable.Fragment_android_name);
        try {
            mFragment = (Fragment) Class.forName(name).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Error when constructing fragment");
        }
        mTag = typedArray.getString(R.styleable.Fragment_android_tag);
        typedArray.recycle();
        setHierarchyChangeListener();
    }

    public FragmentView(Context context, Fragment fragment) {
        super(context);
        mFragment = fragment;
        setHierarchyChangeListener();
    }

    private void setHierarchyChangeListener() {
        setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                child.requestApplyInsets();
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FragmentActivity fragmentActivity = requestActivity();
        FragmentManagerViewModel viewModel =
                ViewModelProviders.of(fragmentActivity).get(FragmentManagerViewModel.class);
        if (mFragment.isAdded()) {
            if (mFragment.getRoot().getParent() != null)
                return;
            if (mFragment.getRoot() != null) {
                this.addView(mFragment.getRoot());
            }
        } else {
            if (mTag == null) {
                mTag = mFragment.getClass().getName();
            }
            boolean repeat = viewModel.mController.tagGetFragment(mTag) != null;
            StringBuilder tagBuilder = new StringBuilder(mTag);
            while (repeat) {
                repeat = viewModel.mController.tagGetFragment((tagBuilder.append("+").toString())) != null;
            }
            mTag = tagBuilder.toString();
            mFragment.initialize(mTag, getId());
            mFragment.dispatchOnAttach(fragmentActivity);
            mFragment.dispatchOnCreate(mFragment.getArguments());
            Fragment parentFragment = getParentFragment();
            if (parentFragment == null) {
                viewModel.mController.add(mFragment);
            } else {
                viewModel.mController.add(parentFragment, mFragment);
            }
            if (!mFragment.isInLayout()) {
                mFragment.dispatchOnViewCreated(mFragment.onCreateView(
                        fragmentActivity.getLayoutInflater().cloneInContext(fragmentActivity.getApplicationContext()),
                        mFragment.getViewContainer(),
                        mFragment.getArguments()
                ));
            }
            mFragment.onActivityCreated(viewModel.mController.mSavedInstanceState);
            mFragment.dispatchAdd();
        }
        mFragment.getViewLifecycleOwner().getLifecycle().addObserver(this);
    }

    public Fragment getFragment() {
        return mFragment;
    }

    @Nullable
    private Fragment getParentFragment() {
        FragmentActivity fragmentActivity = requestActivity();
        ViewGroup viewGroup = (ViewGroup) getParent();
        while (viewGroup != null) {
            if (viewGroup.getId() == android.R.id.content) {
                return null;
            }
            Fragment fragment = fragmentActivity.getFragmentPlusManager().findFragmentById(viewGroup.getId());
            if (fragment != null)
                return fragment;
            if (viewGroup.getParent() != null) {
                viewGroup = (ViewGroup) viewGroup.getParent();
            }
        }
        return null;
    }

    @Nullable
    private FragmentActivity getActivity() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        while (viewGroup != null) {
            if (viewGroup.getId() == android.R.id.content) {
                return (FragmentActivity) viewGroup.getContext();
            }
            if (viewGroup.getParent() != null) {
                viewGroup = (ViewGroup) viewGroup.getParent();
            }
        }
        return null;
    }

    @NonNull
    private FragmentActivity requestActivity() {
        FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity == null) {
            throw new NullPointerException("FragmentActivity not found");
        }
        return fragmentActivity;
    }

    @Override
    public void observer(Lifecycle.Event state) {
        if (state == Lifecycle.Event.ON_DESTROY) {
            mFragment = null;
        }
    }
}
