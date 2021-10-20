package mandysax.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mandysax.core.R;

/**
 * @author Administrator
 */
public class FragmentView extends FrameLayout {

    private final String mName;
    private final String mTag;
    private Fragment mFragment;
    private FragmentActivity mActivity;

    public FragmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        }
        @SuppressLint({"Recycle", "CustomViewStyleable"}) TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Fragment);
        mName = typedArray.getString(R.styleable.Fragment_android_name);
        mTag = typedArray.getString(R.styleable.Fragment_android_tag);
        typedArray.recycle();
    }

    public FragmentView(Context context) {
        super(context);
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
        }
        mName = null;
        mTag = null;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!getFragment().isAdded()) {
            getActivity().getFragmentPlusManager().beginTransaction().add(getId(), getFragment(), getTag()).commit();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFragment = null;
        mActivity = null;
    }

    public String getName() {
        return mName;
    }

    public Fragment getFragment() {
        if (mFragment != null) {
            return mFragment;
        }
        mFragment = getActivity().getFragmentPlusManager().findFragmentByTag(getTag());
        if (mFragment != null) {
            return mFragment;
        }
        try {
            return mFragment = (Fragment) Class.forName(getName()).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Error when constructing fragment");
        }
    }

    @Override
    public String getTag() {
        return mTag == null ? "" + getId() : mTag;
    }

    public FragmentActivity getActivity() {
        if (mActivity != null) {
            return mActivity;
        }
        if (getContext() instanceof FragmentActivity) {
            return mActivity = (FragmentActivity) getContext();
        }
        ViewGroup viewGroup = (ViewGroup) getParent();
        while (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i).getContext() instanceof FragmentActivity) {
                    return (FragmentActivity) viewGroup.getChildAt(i).getContext();
                }
            }
            viewGroup = (ViewGroup) viewGroup.getParent();
        }
        return null;
    }

}
