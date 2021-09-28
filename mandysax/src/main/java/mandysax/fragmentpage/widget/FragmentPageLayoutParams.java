package mandysax.fragmentpage.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mandysax.R;

/**
 * @author liuxiaoliu66
 */
public class FragmentPageLayoutParams extends FrameLayout.LayoutParams {

    public final boolean isDefaultNavHost;

    public FragmentPageLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
        TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.FragmentPage);
        isDefaultNavHost = typedArray.getBoolean(R.styleable.FragmentPage_defaultNavHost, false);
        typedArray.recycle();
    }

    public FragmentPageLayoutParams(ViewGroup.LayoutParams lp) {
        super(lp);
        isDefaultNavHost = false;
    }

}
