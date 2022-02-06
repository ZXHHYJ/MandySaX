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

    private final String mName;
    private final String mTag;
    private Fragment mFragment;
    private FragmentActivity mFragmentActivity;

    @SuppressLint("CustomViewStyleable")
    public FragmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Fragment);
        mName = typedArray.getString(R.styleable.Fragment_android_name);
        String tag = typedArray.getString(R.styleable.Fragment_android_tag);
        if (tag == null)
            tag = mName + getId();
        mTag = tag;
        typedArray.recycle();
        setHierarchyChangeListener();
    }

    public FragmentView(Context context, @NonNull Fragment fragment) {
        super(context);
        mName = null;
        mTag = fragment.toString();
        mFragment = fragment;
        fragment.getViewLifecycleOwner().getLifecycle().addObserver(this);
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
        mFragment = getFragment();

        FragmentManagerViewModel viewModel =
                ViewModelProviders.of(requestActivity()).get(FragmentManagerViewModel.class);

        if (mFragment.isAdded()) {
            if (mFragment.getRoot() != null && mFragment.getRoot().getParent() == null) {
                addView(mFragment.getRoot());
            }
            return;
        }
        Fragment parentFragment = getParentFragment();
        viewModel.mController.dispatchAdd(parentFragment, mFragment, getId(), getTag());
        mFragment.initialize(getTag(), getId());
    }

    @Nullable
    public String getTag() {
        return mTag;
    }

    public Fragment getFragment() {
        if (mFragment == null) {
            mFragment = requestActivity().getFragmentPlusManager().findFragmentByTag(getTag());
        }
        if (mFragment == null && mName != null) {
            try {
                mFragment = (Fragment) Class.forName(mName).newInstance();
                mFragment.getViewLifecycleOwner().getLifecycle().addObserver(this);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("Failed to instantiate fragment");
            }
        }
        return mFragment;
    }

    @Nullable
    private Fragment getParentFragment() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        while (viewGroup != null) {
            if (viewGroup.getId() == android.R.id.content) {
                return null;
            }
            Fragment fragment = requestActivity().getFragmentPlusManager().findFragmentById(viewGroup.getId());
            if (fragment != null)
                return fragment;
            if (viewGroup.getParent() != null) {
                viewGroup = (ViewGroup) viewGroup.getParent();
            }
        }
        return null;
    }

    @NonNull
    private FragmentActivity requestActivity() {
        if (mFragmentActivity != null)
            return mFragmentActivity;
        ViewGroup viewGroup = (ViewGroup) getParent();
        while (viewGroup != null) {
            if (viewGroup.getId() == android.R.id.content) {
                mFragmentActivity = (FragmentActivity) viewGroup.getContext();
                mFragmentActivity.getLifecycle().addObserver(this);
                return mFragmentActivity;
            }
            if (viewGroup.getParent() != null) {
                viewGroup = (ViewGroup) viewGroup.getParent();
            }
        }
        throw new NullPointerException("FragmentActivity not found");
    }

    @Override
    public void observer(Lifecycle.Event state) {
        if (state == Lifecycle.Event.ON_DESTROY) {
            mFragmentActivity = null;
            mFragment = null;
        }
    }
}
