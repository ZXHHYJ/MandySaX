package studio.mandysa.jiuwo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.BindingViewHolder>() {

    private var onBind: (BindingViewHolder.() -> Unit)? = null

    private var onCreate: (ViewCreate.() -> Unit)? = null

    var headers: List<Any?>? = null
        set(value) {
            if (value == null) {
                field = value
                field?.let { notifyItemInserted(it.size) }
                return
            }
            field = value
            if (field != null)
                notifyItemRangeChanged(0, field!!.size)
        }

    var models: List<Any?>? = null
        set(value) {
            if (value == null) {
                field = value
                field?.let { notifyItemInserted(headers?.size ?: 0 + it.size) }
                return
            }
            field = value
            if (field != null)
                notifyItemRangeChanged(headers?.size ?: 0, headers?.size ?: 0 + field!!.size)
        }

    var footers: List<Any?>? = null
        set(value) {
            if (value == null) {
                field = value
                field?.let {
                    notifyItemInserted(
                        headers?.size ?: 0 + (models?.size ?: 0) + it.size
                    )
                }
                return
            }
            field = value
            if (field != null)
                notifyItemRangeChanged(headers?.size ?: 0 + (models?.size ?: 0), itemCount)
        }

    var mModel: Any? = null

    fun onBind(block: BindingViewHolder.() -> Unit) {
        onBind = block
    }

    fun onCreate(block: ViewCreate.() -> Unit) {
        onCreate = block
    }

    val type = HashMap<Class<*>, Int>()

    inline fun <reified M> addType(@LayoutRes id: Int) {
        type[M::class.java] = id
    }

    inline fun <reified M> getModel(): M = mModel as M

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

    override fun onViewAttachedToWindow(holder: BindingViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttached?.invoke(holder)
    }

    override fun onViewDetachedFromWindow(holder: BindingViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached?.invoke(holder)
    }

    override fun onViewRecycled(holder: BindingViewHolder) {
        super.onViewRecycled(holder)
        holder.onRecycled?.invoke(holder)
    }

    override fun getItemCount(): Int {
        return (headers?.size ?: 0) + (models?.size ?: 0) + (footers?.size ?: 0)
    }

    override fun getItemViewType(position: Int): Int {
        return type[getModel(position)::class.java]!!
    }

    fun addHeader(model: Any) {
        addHeader(model, headers?.size ?: 0)
    }

    fun addHeader(model: Any, position: Int) {
        if (headers != null) {
            var list = headers?.toMutableList()
            if (list == null)
                list = ArrayList()
            list.add(position, model)
            headers = list
            return
        }
        headers = listOf(model)
    }

    fun addModels(models: List<Any>) {
        addModels(models, this.models?.size ?: 0)
    }

    fun addModels(models: List<Any>, position: Int) {
        if (this.models != null) {
            var list = this.models?.toMutableList()
            if (list == null)
                list = ArrayList()
            list.addAll(position, models)
            this.models = list
            return
        }
        this.models = models
    }

    fun addFooter(model: Any) {
        addFooter(model, footers?.size ?: 0)
    }

    fun addFooter(model: Any, position: Int) {
        if (footers != null) {
            var list = footers?.toMutableList()
            if (list == null)
                list = ArrayList()
            list.add(position, model)
            footers = list
            return
        }
        footers = listOf(model)
    }

    fun clearModels() {
        val size = itemCount
        headers = ArrayList()
        models = ArrayList()
        footers = ArrayList()
        notifyItemRemoved(size)
    }

    private fun getModel(position: Int): Any {
        val headerSize = headers?.size ?: 0
        val modelSize = models?.size ?: 0
        return when {
            position < headerSize -> {
                headers?.get(position)
            }
            position >= headerSize && position < modelSize + headerSize -> {
                models?.get(position - headerSize)
            }
            else -> {
                footers?.get(position - headerSize - modelSize)
            }
        } ?: throw NullPointerException("list has empty model.")
    }

    inner class BindingViewHolder(viewCreate: ViewCreate) :
        RecyclerView.ViewHolder(viewCreate.view) {

        internal var onAttached: (BindingViewHolder.() -> Unit)? = null

        internal var onDetached: (BindingViewHolder.() -> Unit)? = null

        internal var onRecycled: (BindingViewHolder.() -> Unit)? = null

        fun onAttached(block: BindingViewHolder.() -> Unit) {
            onAttached = block
        }

        fun onDetached(block: BindingViewHolder.() -> Unit) {
            onDetached = block
        }

        fun onRecycled(block: BindingViewHolder.() -> Unit) {
            onRecycled = block
        }

        val headerSize get() = headers?.size ?: 0

        val modelSize get() = this@RecyclerAdapter.models?.size ?: 0

        val footerSize get() = footers?.size ?: 0

        val models: List<Any?>?
            get() = if (layoutPosition < headerSize) {
                headers
            } else
                if (layoutPosition >= headerSize && layoutPosition < modelSize + headerSize) {
                    this@RecyclerAdapter.models
                } else {
                    footers
                }

        val modelPosition
            get():Int {
                return if (layoutPosition < headerSize) {
                    layoutPosition
                } else
                    if (layoutPosition >= headerSize && layoutPosition < modelSize + headerSize) {
                        layoutPosition - headerSize
                    } else {
                        (layoutPosition - headerSize - modelSize)
                    }
            }
    }

}