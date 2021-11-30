package studio.mandysa.bottomnavigationbar

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import mandysax.lifecycle.livedata.MutableLiveData
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.jiuwo.utils.staggered


class BottomNavigationBar : RecyclerView {

    @ColorInt
    private var mActiveColor = context.getColor(R.color.default_checked_color)

    @ColorInt
    private var mInActiveColor = context.getColor(R.color.default_unchecked_color)

    private val mOrientation: Int

    private val mSelectedPosition: MutableLiveData<Int?> = MutableLiveData(null)

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

    fun getSelectedPosition(): MutableLiveData<Int?> {
        return mSelectedPosition
    }

    fun setSelectedPosition(position: Int) {
        mSelectedPosition.value = position
    }

    constructor(context: Context) : super(context) {
        mOrientation = VERTICAL
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationBar)
        mOrientation = typedArray.getInt(
            R.styleable.BottomNavigationBar_android_orientation,
            VERTICAL
        )
        typedArray.recycle()
    }

    var models: List<BottomNavigationItem>? = null
        set(value) {
            field = value
            (if (mOrientation != HORIZONTAL)
                staggered(field!!.size, orientation = mOrientation)
            else linear()).setup {
                addType<BottomNavigationItem>(if (mOrientation == HORIZONTAL) R.layout.item_horizontal else R.layout.item_vertical)
                onBind {
                    val model = getModel<BottomNavigationItem>()
                    val itemIcon = itemView.findViewById<ImageView>(R.id.image1)
                    val itemTitle = itemView.findViewById<TextView?>(R.id.text1)
                    itemIcon.setImageResource(model.mImageRes)
                    itemIcon.setColorFilter(mInActiveColor)
                    itemTitle.text = model.mText
                    itemTitle.setTextColor(mInActiveColor)
                    itemView.setOnClickListener {
                        mSelectedPosition.value = modelPosition
                    }
                    mSelectedPosition.observeForever {
                        (if (it == modelPosition)
                            if (model.mActiveColor == null) mActiveColor else model.mActiveColor!!
                        else
                            if (model.mInActiveColor == null) mInActiveColor else model.mInActiveColor!!).apply {
                            itemIcon.setColorFilter(
                                this
                            )
                            itemTitle.setTextColor(
                                this
                            )
                        }
                    }
                }
            }
            recyclerAdapter.models = field
        }

}