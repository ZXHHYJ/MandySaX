package mandysax.fragmentpage.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import mandysax.fragment.FragmentView;

import java.util.ArrayList;

/**
 * @author liuxiaoliu66
 */
public class FragmentPage extends FrameLayout {

    private int mPosition = -1;

    private final ArrayList<FragmentView> mViews = new ArrayList<>();

    public FragmentPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addFragmentView(View view, ViewGroup.LayoutParams params) {
        if (view instanceof FragmentView) {
            FragmentView fragmentView = (FragmentView) view;
            mViews.add(fragmentView);
            if (params instanceof LayoutParams) {
                FragmentPageLayoutParams layoutParams = (FragmentPageLayoutParams) params;
                if (!layoutParams.isDefaultNavHost) {
                    fragmentView.setVisibility(View.GONE);
                } else {
                    mPosition = mViews.size() - 1;
                }
            }
        }
    }

    public void setPosition(int position) {
        mPosition = position;
        for (FragmentView fragmentView : mViews) {
            if (!fragmentView.equals(mViews.get(position))) {
                fragmentView.setVisibility(View.GONE);
            } else {
                fragmentView.setVisibility(View.VISIBLE);
            }
        }
    }

    public int getPosition() {
        return mPosition;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        addFragmentView(child, params);
    }

    @Override
    protected FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new FragmentPageLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new FragmentPageLayoutParams(lp);
    }

    @Override
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FragmentPageLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof FragmentPageLayoutParams;
    }
}
