package studio.mandysa.jiuwo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.BindingViewHolder>() {

    private var onBind: (BindingViewHolder.() -> Unit)? = null

    private var onCreate: (ViewCreate.() -> Unit)? = null

    var headers: List<Any?>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var models: List<Any?>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mModel: Any? = null

    fun onBind(block: BindingViewHolder.() -> Unit) {
        onBind = block
    }

    fun onCreate(block: ViewCreate.() -> Unit) {
        onCreate = block
    }

    val mType = HashMap<Class<*>, Int>()

    inline fun <reified M> addType(@LayoutRes id: Int) {
        mType[M::class.java] = id
    }

    inline fun <reified M> getModel(): M = mModel as M

    inline fun <reified M> getModelOrNull(): M? = mModel as? M

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val viewCreate = ViewCreate(viewType)
        onCreate?.invoke(viewCreate)
        if (viewCreate.view == null) {
            viewCreate.view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        }
        return BindingViewHolder(viewCreate)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        mModel = getModel(position)
        onBind?.invoke(holder)
    }

    override fun getItemCount(): Int {
        var i = models?.size
        if (i == null) i = 0;
        var e = headers?.size
        if (e == null) e = 0;
        return i + e
    }

    override fun getItemViewType(position: Int): Int {
        return mType[getModel(position)::class.java]!!
    }

    fun addModels(models: List<Any>) {
        this.models =
            if (this.models == null) models else this.models!!.toMutableList().also {
                it.addAll(models)
            }
        notifyItemInserted(models.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addHeader(model: Any) {
        headers = if (headers == null)
            listOf(model)
        else headers!!.toMutableList().also {
            it.add(model)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearModels() {
        models = ArrayList()
        headers = ArrayList()
        notifyDataSetChanged()
    }

    private fun getModel(position: Int): Any {
        var headerSize = headers?.size
        if (headerSize == null) headerSize = 0
        return if (position >= headerSize) models!![position-headerSize]!! else headers!![position]!!
    }

    inner class BindingViewHolder(viewCreate: ViewCreate) :
        RecyclerView.ViewHolder(viewCreate.view) {
        val modelPosition get() = layoutPosition - if (headers != null) headers!!.size else 0
    }

}