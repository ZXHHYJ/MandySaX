package mandysax.core.view;

import android.graphics.Insets;
import android.os.Build;
import android.view.WindowInsets;

import static android.os.Build.VERSION_CODES.R;

/**
 * @author liuxiaoliu66
 */
public final class WindowInsetsCompat {
    private final int mTop, mLeft, mRight, mBottom;

    private final WindowInsets mInsets;

    @SuppressWarnings("ALL")
    public WindowInsetsCompat(WindowInsets insets) {
        mInsets = insets;
        if (Build.VERSION.SDK_INT < R) {
            mTop = insets.getSystemWindowInsetTop();
            mLeft = insets.getSystemWindowInsetLeft();
            mRight = insets.getSystemWindowInsetRight();
            mBottom = insets.getSystemWindowInsetBottom();
        } else {
            mTop = insets.getInsets(WindowInsets.Type.systemBars()).top;
            mLeft = insets.getInsets(WindowInsets.Type.displayCutout()).left;
            mRight = insets.getInsets(WindowInsets.Type.displayCutout()).right;
            mBottom = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        }
    }

    public InsetsCompat getInsets(int type) {
        if (Build.VERSION.SDK_INT >= R) {
            Insets insets = mInsets.getInsets(type);
            return new InsetsCompat(insets.left, insets.top, insets.right, insets.bottom);
        } else {
            return new InsetsCompat(mLeft, mTop, mRight, mBottom);
        }
    }

    public int getSystemWindowInsetTop() {
        return mTop;
    }

    public int getSystemWindowInsetLeft() {
        return mLeft;
    }

    public int getSystemWindowInsetRight() {
        return mRight;
    }

    public int getSystemWindowInsetBottom() {
        return mBottom;
    }

}
