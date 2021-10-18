package studio.mandysa.jiuwo.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter : RecyclerView.Adapter<BindingViewHolder>() {

    private var onBind: (BindingViewHolder.() -> Unit)? = null

    private var mModels: MutableList<Any?>? = null

    var model: Any? = null

    fun onBind(block: BindingViewHolder.() -> Unit) {
        onBind = block
    }

    val mType = HashMap<Class<*>, Int>()

    inline fun <reified M> addType(@LayoutRes id: Int) {
        mType[M::class.java] = id
    }

    inline fun <reified M> RecyclerAdapter.getModel(): M = model as M

    inline fun <reified M> RecyclerAdapter.getModelOrNull(): M? = model as? M

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        if (mModels != null) {
            for (any in mModels!!) {
                if (any != null)
                    for (type in mType) {
                        if (type.key == any::class.java) {
                            return BindingViewHolder(
                                parent, type.value
                            )
                        }
                    }
            }
        }
        return null!!
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        model = mModels?.get(position)
        onBind?.invoke(holder)
    }

    override fun getItemCount(): Int {
        if (mModels == null) return 0
        return mModels!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addModels(models: List<Any>) {
        mModels = if (mModels == null) {
            models.toMutableList()
        } else {
            val data = mModels
            data!!.addAll(models)
            data
        }
        notifyItemInserted(models.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearModels() {
        mModels?.clear()
        notifyDataSetChanged()
    }
}