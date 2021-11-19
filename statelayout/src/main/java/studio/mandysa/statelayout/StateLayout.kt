package studio.mandysa.statelayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class StateLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    companion object {
        @JvmStatic
        var emptyLayout: Int = -1

        @JvmStatic
        var loadingLayout: Int = -1

        @JvmStatic
        var errorLayout: Int = -1

        @JvmStatic
        var empty: (View.() -> Unit)? = null

        @JvmStatic
        var loading: (View.() -> Unit)? = null

        @JvmStatic
        var error: (View.() -> Unit)? = null

        private var retryId = -1
        fun setRetryId(retryId: Int) {
            this.retryId = retryId
        }
    }

    private var mEmpty: (View.() -> Unit)? = null

    private var mLoading: (View.() -> Unit)? = null

    private var mError: (View.() -> Unit)? = null

    private var mContent: (View.() -> Unit)? = null

    private var mEmptyLayoutId: Int

    private var mLoadingLayoutId: Int

    private var mErrorLayoutId: Int

    private var mEmptyView: View? = null

    private var mLoadingView: View? = null

    private var mErrorView: View? = null

    private var mContentView: View? = null

    private var mRetryId = retryId

    fun setEmptyLayoutId(id: Int) {
        mEmptyLayoutId = id
    }

    fun setLoadingLayoutId(id: Int) {
        mLoadingLayoutId = id
    }

    fun setErrorLayoutId(id: Int) {
        mErrorLayoutId = id
    }

    fun setRetryId(retryId: Int) {
        mRetryId = retryId
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        if (mContentView == null) mContentView = child.also { child?.visibility = View.GONE }!!
    }

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        mEmptyLayoutId = typedArray.getResourceId(R.styleable.StateLayout_emptyLayout, emptyLayout)
        mLoadingLayoutId =
            typedArray.getResourceId(R.styleable.StateLayout_loadingLayout, loadingLayout)
        mErrorLayoutId = typedArray.getResourceId(R.styleable.StateLayout_errorLayout, errorLayout)
        typedArray.recycle()
    }

    /**
     * 设置显示内容视图执行事件
     */
    fun showContent(block: View.() -> Unit) {
        mContent = block
    }

    /**
     * 设置显示空视图执行事件
     */
    fun showEmpty(block: View.() -> Unit) {
        mEmpty = block
    }

    /**
     * 设置显示加载试图执行事件
     */
    fun showLoading(block: View.() -> Unit) {
        mLoading = block
    }

    /**
     * 设置显示错误视图执行事件
     */
    fun showError(block: View.() -> Unit) {
        mError = block
    }

    fun showEmptyState() {
        if (mEmptyView == null)
            addView(
                LayoutInflater.from(context).inflate(mEmptyLayoutId, this, false)
                    .also {
                        mEmptyView = it
                        setRetryEvent(it)
                    }
            )
        hideAllViews(mEmptyView!!)?.apply {
            visibility = View.VISIBLE
            empty?.invoke(this)
            mEmpty?.invoke(this)
        }
    }

    fun showLoadingState() {
        if (mLoadingView == null)
            addView(
                LayoutInflater.from(context).inflate(mLoadingLayoutId, this, false)
                    .also {
                        mLoadingView = it
                    }
            )
        hideAllViews(mLoadingView!!)?.apply {
            visibility = View.VISIBLE
            loading?.invoke(this)
            mLoading?.invoke(this)
        }
    }

    fun showErrorState() {
        if (mErrorView == null)
            addView(
                LayoutInflater.from(context).inflate(mErrorLayoutId, this, false)
                    .also {
                        mErrorView = it
                        setRetryEvent(it)
                    }
            )
        hideAllViews(mErrorView!!)?.apply {
            visibility = View.VISIBLE
            error?.invoke(this)
            mError?.invoke(this)
        }
    }

    fun showContentState() {
        hideAllViews(mContentView!!)?.apply {
            visibility = View.VISIBLE
            mContent?.invoke(this)
        }
    }

    private fun setRetryEvent(view: View) {
        if (mRetryId != -1)
            view.findViewById<View?>(mRetryId)?.setOnClickListener {
                showLoadingState()
            }
    }

    private fun hideAllViews(view: View): View? {
        var dout = false
        for (index in 0 until childCount) {
            getChildAt(index).apply {
                if (this.equals(view)) {
                    dout = true
                } else
                    visibility = View.GONE
            }
        }
        return if (dout) view else null
    }

}