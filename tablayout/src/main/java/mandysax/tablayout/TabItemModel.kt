package mandysax.tablayout

import android.view.View
import androidx.annotation.ColorInt

/**
 * @author ZXHHYJ
 */
interface TabItemModel {
    /**
     * 获取itemView布局id
     *
     * @return 布局id
     */

    @ColorInt
    fun getLayoutId(): Int

    /**
     * 修改Item状态
     *
     * @param itemView itemView
     * @param active   是否选中
     */
    fun active(itemView: View, active: Boolean)
    fun setActiveColor(@ColorInt color: Int): TabItemModel?
    fun setInActiveColor(@ColorInt color: Int): TabItemModel?
}

fun List<TabItemModel>.setActiveColor(@ColorInt color: Int): List<TabItemModel> {
    forEach {
        it.setActiveColor(color)
    }
    return this
}

fun List<TabItemModel>.setInActiveColor(@ColorInt color: Int): List<TabItemModel> {
    forEach {
        it.setInActiveColor(color)
    }
    return this
}