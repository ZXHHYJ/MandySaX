package mandysax.tablayout

import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt

class NavigationItem(
    private val text: String
) : TabItemModel {
    @ColorInt
    private var mActiveColor: Int? = null

    @ColorInt
    private var mInActiveColor: Int? = null

    override fun setActiveColor(@ColorInt color: Int): NavigationItem {
        mActiveColor = color
        return this
    }

    override fun setInActiveColor(@ColorInt color: Int): NavigationItem {
        mInActiveColor = color
        return this
    }

    override fun getLayoutId(): Int = R.layout.navigaction_item

    override fun active(itemView: View, active: Boolean) {
        val itemTitle = itemView.findViewById<TextView>(R.id.text1)
        itemTitle?.text = text
        mInActiveColor?.let {
            itemTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
            itemTitle?.setTextColor(it)
        }
        itemTitle.typeface = if (active)
            Typeface.defaultFromStyle(Typeface.BOLD)
        else Typeface.defaultFromStyle(Typeface.NORMAL)

        itemTitle.setTextSize(
            TypedValue.COMPLEX_UNIT_SP, when (active) {
                true ->
                    21F
                false ->
                    18F
            }
        )
        itemTitle?.setTextColor(
            (if (active)
                mActiveColor
            else
                mInActiveColor)!!
        )
    }

}