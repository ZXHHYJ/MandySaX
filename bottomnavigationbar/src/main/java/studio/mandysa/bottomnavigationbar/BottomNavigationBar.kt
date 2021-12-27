package studio.mandysa.bottomnavigationbar

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.jiuwo.utils.staggered


class BottomNavigationBar : RecyclerView {

    @ColorInt
    private var mActiveColor = context.getColor(R.color.default_checked_color)

    @ColorInt
    private var mInActiveColor = context.getColor(R.color.default_unchecked_color)

    private val mSelectedPosition = MutableLiveData<Int>()

    fun setActiveColorResource(@ColorRes colorRes: Int): BottomNavigationBar {
        return setActiveColor(context.getColor(colorRes))
    }

    fun setActiveColor(@ColorInt color: Int): BottomNavigationBar {
        mActiveColor = color
        return this
    }

    fun setInActiveColorResource(@ColorRes colorRes: Int): BottomNavigationBar {
        return setInActiveColor(context.getColor(colorRes))
    }

    fun setInActiveColor(@ColorInt color: Int): BottomNavigationBar {
        mInActiveColor = color
        return this
    }

    fun getSelectedPosition(): LiveData<Int> {
        return mSelectedPosition
    }

    fun setSelectedPosition(position: Int) {
        mSelectedPosition.value = position
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    var models: List<BottomNavigationItem>? = null
        set(value) {
            field = value
            staggered(field!!.size).setup {
                addType<BottomNavigationItem>(R.layout.item_vertical)
                onBind {
                    val model = getModel<BottomNavigationItem>()
                    val itemIcon = itemView.findViewById<ImageView>(R.id.image1)
                    val itemTitle = itemView.findViewById<TextView>(R.id.text1)
                    itemIcon.setImageResource(model.mImageRes)
                    itemIcon.setColorFilter(mInActiveColor)
                    itemTitle?.text = model.mText
                    itemTitle?.setTextColor(mInActiveColor)
                    itemView.setOnClickListener {
                        setSelectedPosition(modelPosition)
                    }
                    mSelectedPosition.observeForever { it ->
                        when (it == modelPosition) {
                            true -> {
                                if (model.mActiveColor == null) mActiveColor else model.mActiveColor!!
                            }
                            false -> {
                                if (model.mInActiveColor == null) mInActiveColor else model.mInActiveColor!!
                            }
                        }.apply {
                            itemIcon.setColorFilter(this, PorterDuff.Mode.SRC_IN)
                            itemTitle?.setTextColor(
                                this
                            )
                        }
                    }
                }
            }
            recyclerAdapter.models = field
        }

}