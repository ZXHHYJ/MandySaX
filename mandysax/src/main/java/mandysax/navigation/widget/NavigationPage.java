package mandysax.navigation.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import mandysax.R;

public class NavigationPage extends FrameLayout {

    private final ArrayList<Navigation> mViews = new ArrayList<>();

    private int mPosition = -1;

    public NavigationPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void showNavigation(int position) {
        mPosition = position;
        for (Navigation navigation : mViews) {
            navigation.setVisibility(View.GONE);
        }
        mViews.get(position).setVisibility(View.VISIBLE);
    }

    public int getPosition() {
        return mPosition;
    }

    void securityCheck(View view) {
        if (!(view instanceof Navigation)) {
            throw new ClassCastException("The added view must be Navigation View");
        }
    }

    void addNavigation(View child, ViewGroup.LayoutParams params) {
        Navigation navigation = (Navigation) child;
        mViews.add(navigation);
        if (checkLayoutParams(params)) {
            NavigationLayoutParams layoutParams = (NavigationLayoutParams) params;
            if (!layoutParams.isDefaultNavHost) {
                child.setVisibility(View.GONE);
            } else mPosition = mViews.size() - 1;
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        securityCheck(child);
        addNavigation(child, params);
    }

    public boolean onBackPressed() {
        return mViews.get(mPosition).onBackPressed();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new NavigationLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new NavigationLayoutParams(lp);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new NavigationLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof NavigationLayoutParams;
    }

    public static class NavigationLayoutParams extends FrameLayout.LayoutParams {

        final boolean isDefaultNavHost;

        public NavigationLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            @SuppressLint({"Recycle", "CustomViewStyleable"}) TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.NavigationPage);
            isDefaultNavHost = typedArray.getBoolean(R.styleable.NavigationPage_defaultNavHost, false);
            typedArray.recycle();
        }

        public NavigationLayoutParams(ViewGroup.LayoutParams lp) {
            super(lp);
            isDefaultNavHost = false;
        }

    }
}
