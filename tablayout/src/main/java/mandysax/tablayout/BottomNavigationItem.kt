package mandysax.tablayout

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

class BottomNavigationItem constructor(
    @DrawableRes private val imageRes: Int,
    private val text: String
) : TabItemModel {
    @ColorInt
    private var mActiveColor: Int? = null

    @ColorInt
    private var mInActiveColor: Int? = null

    override fun setActiveColor(@ColorInt color: Int): BottomNavigationItem {
        mActiveColor = color
        return this
    }

    override fun setInActiveColor(@ColorInt color: Int): BottomNavigationItem {
        mInActiveColor = color
        return this
    }

    override fun getLayoutId(): Int = R.layout.bottom_navigaction_item

    override fun active(itemView: View, active: Boolean) {
        val itemIcon = itemView.findViewById<ImageView>(R.id.image1)
        val itemTitle = itemView.findViewById<TextView>(R.id.text1)
        itemIcon.setImageResource(imageRes)
        itemTitle?.text = text
        when (active) {
            true ->
                mActiveColor
            false ->
                mInActiveColor
        }?.let {
            itemIcon.setColorFilter(it, PorterDuff.Mode.SRC_IN)
            itemTitle?.setTextColor(
                it
            )
        }
    }

}