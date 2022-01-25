package mandysax.tablayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.jiuwo.utils.staggered

/**
 * @author ZXHHYJ
 */
open class TabLayout : RecyclerView {

    private val mSelectedPosition = MutableLiveData<Int>()

    private var mTabModel: Int = 0

    fun getSelectedPosition(): LiveData<Int> {
        return mSelectedPosition
    }

    fun setSelectedPosition(position: Int) {
        mSelectedPosition.value = position
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.TabLayout)
        mTabModel = typedArray.getInt(R.styleable.TabLayout_tabModel, 0)
        typedArray.recycle()
    }

    var models: List<TabItemModel>? = null
        set(value) {
            field = value
            when (mTabModel) {
                0 -> staggered(field!!.size)
                1 -> linear(HORIZONTAL)
                else -> null
            }?.setup {
                type[field!![0]::class.java] = field!![0].getLayoutId()
                onBind {
                    val model = getModel<TabItemModel>()
                    model.active(itemView, false)
                    itemView.setOnClickListener {
                        setSelectedPosition(modelPosition)
                    }
                    mSelectedPosition.observeForever {
                        model.active(itemView, it == modelPosition)
                    }
                }
            }
            recyclerAdapter.models = field
        }
}