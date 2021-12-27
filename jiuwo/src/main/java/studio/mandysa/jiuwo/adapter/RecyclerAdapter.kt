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

    var footers: List<Any?>? = null
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
        return mType[getModel(position)::class.java]!!
    }

    fun addHeader(model: Any) {
        if (headers == null)
            headers = listOf(model) else {
            (headers as MutableList).add(model)
            notifyItemInserted(1)
        }
    }

    fun addModels(models: List<Any>) {
        if (this.models == null) this.models = models else {
            (this.models as MutableList).addAll(
                models
            )
            notifyItemInserted(models.size)
        }
    }

    fun addFooter(model: Any) {
        if (footers == null)
            footers = listOf(model) else {
            (footers as MutableList).add(model)
            notifyItemInserted(1)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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
        return if (position < headerSize) {
            headers ?: listIsEmpty("header")
            headers!![position]!!
        } else
            if (position >= headerSize && position < modelSize + headerSize) {
                models ?: listIsEmpty("model")
                models!![position - headerSize]!!
            } else {
                footers ?: listIsEmpty("footer")
                footers!![position - headerSize - modelSize]!!
            }
    }

    private fun listIsEmpty(listName: String) {
        throw NullPointerException("$listName Is Null!")
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

        val modelPosition
            get():Int {
                val headerSize = headers?.size ?: 0
                val modelSize = models?.size ?: 0
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