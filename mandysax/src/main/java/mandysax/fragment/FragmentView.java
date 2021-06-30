package mandysax.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import mandysax.R;

public class FragmentView extends FrameLayout {

    private final String mName;

    private final String mTag;

    private Fragment mFragment;

    public FragmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint({"Recycle", "CustomViewStyleable"}) TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Fragment);
        mName = typedArray.getString(R.styleable.Fragment_android_name);
        mTag = typedArray.getString(R.styleable.Fragment_android_tag);
        typedArray.recycle();
        if (getId() == View.NO_ID)
            setId(View.generateViewId());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!getFragment().isAdded())
            getActivity().getFragmentPlusManager().add(getId(), getFragment(), getTag()).commitNow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFragment = null;
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
        return mTag == null ? getName() + getId() : mTag;
    }

    public FragmentActivity getActivity() {
        return (FragmentActivity) getContext();
    }

}
