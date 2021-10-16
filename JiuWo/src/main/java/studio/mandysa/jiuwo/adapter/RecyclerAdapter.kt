package studio.mandysa.jiuwo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RecyclerAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var mItemViewType: Int = -1

    private var mItemView: View? = null

    private var onBind: (RecyclerAdapter.() -> Unit)? = null

    var models: List<Any?>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    var model: Any? = null

    fun onBind(block: RecyclerAdapter.() -> Unit) {
        onBind = block
    }

    val mType = HashMap<Class<*>, Int>()

    inline fun <reified M> RecyclerAdapter.addType(@LayoutRes id: Int) {
        mType[M::class.java] = id
    }

    fun <M> RecyclerAdapter.getModel(): M = model as M

    inline fun <reified M> RecyclerAdapter.getModelOrNull(): M? = model as? M

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (models != null) {
            for (any in models!!) {
                if (any != null)
                    for (type in mType) {
                        if (type.key == any::class.java) {
                            mItemViewType = type.value
                            return BindingViewHolder(
                                LayoutInflater.from(parent.context).inflate(
                                    type.value,
                                    parent, false
                                )
                            );
                        }
                    }
            }
        }
        return null!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        model = models?.get(position)
        mItemView = holder.itemView
        onBind?.invoke(this)
    }

    override fun getItemCount(): Int {
        if (models == null) return 0
        return models!!.size
    }

    fun getItemViewType(): Int {
        return mItemViewType
    }

    fun getItemView(): View {
        return mItemView!!
    }
}