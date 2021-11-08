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
    }

    private var mEmpty: (View.() -> Unit)? = empty

    private var mLoading: (View.() -> Unit)? = loading

    private var mError: (View.() -> Unit)? = error

    private var mContent: (View.() -> Unit)? = null

    private val mEmptyLayout: Int

    private val mLoadingLayout: Int

    private val mErrorLayout: Int

    private var mEmptyView: View? = null

    private var mLoadingView: View? = null

    private var mErrorView: View? = null

    private var mContentView: View? = null

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        if (mContentView == null) mContentView = child!!
    }

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        mEmptyLayout = typedArray.getResourceId(R.styleable.StateLayout_emptyLayout, emptyLayout)
        mLoadingLayout =
            typedArray.getResourceId(R.styleable.StateLayout_loadingLayout, loadingLayout)
        mErrorLayout = typedArray.getResourceId(R.styleable.StateLayout_errorLayout, errorLayout)
        typedArray.recycle()
    }

    fun showContent(block: View.() -> Unit) {
        mContent = block
    }

    fun showEmpty(block: View.() -> Unit) {
        mEmpty = block
    }

    fun showLoading(block: View.() -> Unit) {
        mLoading = block
    }

    fun showError(block: View.() -> Unit) {
        mError = block
    }

    fun showEmptyState() {
        if (mEmptyView == null)
            addView(
                LayoutInflater.from(context).inflate(mEmptyLayout, this, false)
                    .also {
                        mEmptyView = it
                    }
            )
        hideAllViews(mErrorView!!)?.apply {
            visibility = View.VISIBLE
            mEmpty?.invoke(this)
        }
    }

    fun showLoadingState() {
        if (mLoadingView == null)
            addView(
                LayoutInflater.from(context).inflate(mLoadingLayout, this, false)
                    .also {
                        mLoadingView = it
                    }
            )
        hideAllViews(mLoadingView!!)?.apply {
            visibility = View.VISIBLE
            mLoading?.invoke(this)
        }
    }

    fun showErrorState() {
        if (mErrorView == null)
            addView(
                LayoutInflater.from(context).inflate(mErrorLayout, this, false)
                    .also {
                        mErrorView = it
                    }
            )
        hideAllViews(mErrorView!!)?.apply {
            visibility = View.VISIBLE
            mError?.invoke(this)
        }
    }

    fun showContentState() {
        hideAllViews(mContentView!!)?.apply {
            visibility = View.VISIBLE
            mContent?.invoke(this)
        }
    }

    private fun hideAllViews(view: View): View? {
        var dout = false
        for (index in 0 until childCount) {
            getChildAt(index).apply {
                if (visibility == View.VISIBLE)
                    if (equals(view)) {
                        dout = true
                        visibility = View.VISIBLE
                    } else
                        visibility = View.GONE
            }
        }
        return if (dout) null else view
    }

}