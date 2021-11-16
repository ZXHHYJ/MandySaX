package studio.mandysa.bottomnavigationbar

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

class BottomNavigationItem constructor(@DrawableRes imageRes: Int, text: String) {
    @ColorInt
    internal var mActiveColor: Int? = null

    @ColorInt
    internal var mInActiveColor: Int? = null

    internal val mImageRes = imageRes;

    internal val mText = text

    fun setActiveColor(@ColorInt color: Int): BottomNavigationItem {
        mActiveColor = color
        return this
    }

    fun setInActiveColor(@ColorInt color: Int): BottomNavigationItem {
        mInActiveColor = color
        return this
    }
}