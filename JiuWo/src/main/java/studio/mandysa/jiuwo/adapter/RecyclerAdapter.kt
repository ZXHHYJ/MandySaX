package studio.mandysa.jiuwo.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter : RecyclerView.Adapter<BindingViewHolder>() {

    private var onBind: (BindingViewHolder.() -> Unit)? = null

    var mModels: MutableList<Any?>? = null

    var mModel: Any? = null

    fun onBind(block: BindingViewHolder.() -> Unit) {
        onBind = block
    }

    val mType = HashMap<Class<*>, Int>()

    inline fun <reified M> addType(@LayoutRes id: Int) {
        mType[M::class.java] = id
        println(M::class.java.toString() + " " + id)
    }

    inline fun <reified M> getModel(): M = mModel as M

    inline fun <reified M> getModelOrNull(): M? = mModel as? M

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(
            parent, mType[mModels!![viewType]!!::class.java]!!
        )
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        mModel = mModels?.get(position)
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