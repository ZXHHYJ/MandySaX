package mandysax.tablayout

import android.content.Context
import android.content.res.TypedArray
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import org.jetbrains.annotations.Contract
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

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.position = mSelectedPosition.value
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        mSelectedPosition.value = ss.position
    }

    internal class SavedState : BaseSavedState {
        var position = 0

        constructor(superState: Parcelable?) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            position = `in`.readValue(javaClass.classLoader) as Int
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeValue(position)
        }

        companion object {
            @JvmField
            val CREATOR: Creator<SavedState> = object : Creator<SavedState> {
                @Contract("_ -> new")
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                @Contract(value = "_ -> new", pure = true)
                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}