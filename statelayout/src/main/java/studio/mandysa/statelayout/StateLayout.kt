package studio.mandysa.statelayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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
        var contentLayout: Int = -1

        @JvmStatic
        var empty: (View.() -> Unit)? = null

        @JvmStatic
        var loading: (View.() -> Unit)? = null

        @JvmStatic
        var error: (View.() -> Unit)? = null

        @JvmStatic
        var content: (View.() -> Unit)? = null
    }

    private var mEmpty: (View.() -> Unit)? = empty

    private var mLoading: (View.() -> Unit)? = loading

    private var mError: (View.() -> Unit)? = error

    private var mContent: (View.() -> Unit)? = content

    private val mEmptyLayout: Int

    private val mLoadingLayout: Int

    private val mErrorLayout: Int

    private val mContentLayout: Int

    private var mEmptyView: View? = null

    private var mLoadingView: View? = null

    private var mErrorView: View? = null

    private var mContentView: View? = null

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        mEmptyLayout = typedArray.getResourceId(R.styleable.StateLayout_emptyLayout, emptyLayout)
        mLoadingLayout =
            typedArray.getResourceId(R.styleable.StateLayout_loadingLayout, loadingLayout)
        mErrorLayout = typedArray.getResourceId(R.styleable.StateLayout_errorLayout, errorLayout)
        mContentLayout =
            typedArray.getResourceId(R.styleable.StateLayout_contentLayout, contentLayout)
        typedArray.recycle()
        val layoutInflater = LayoutInflater.from(context)
        addView(
            layoutInflater.inflate(mEmptyLayout, this, false).also { it.visibility = View.GONE }, 0
        )
        addView(
            layoutInflater.inflate(mLoadingLayout, this, false).also { it.visibility = View.GONE },
            1
        )
        addView(
            layoutInflater.inflate(mErrorLayout, this, false).also { it.visibility = View.GONE }, 2
        )
        addView(
            layoutInflater.inflate(mContentLayout, this, false).also { it.visibility = View.GONE },
            3
        )
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
        hideAllViews()
        if (mEmptyView == null) {
            addView(
                LayoutInflater.from(context).inflate(mEmptyLayout, this, false)
                    .also {
                        mEmptyView = it
                    }, 0
            )
        } else mEmptyView!!.visibility = View.VISIBLE
        mEmpty?.invoke(mEmptyView!!)
    }

    fun showLoadingState() {
        hideAllViews()
        if (mLoadingView == null) {
            addView(
                LayoutInflater.from(context).inflate(mLoadingLayout, this, false)
                    .also {
                        mLoadingView = it
                    }, 0
            )
        } else mLoadingView!!.visibility = View.VISIBLE
        mLoading?.invoke(mLoadingView!!)
    }

    fun showErrorState() {
        hideAllViews()
        if (mErrorView == null) {
            addView(
                LayoutInflater.from(context).inflate(mErrorLayout, this, false)
                    .also {
                        mErrorView = it
                    }, 0
            )
        } else mErrorView!!.visibility = View.VISIBLE
        mError?.invoke(mErrorView!!)
    }

    fun showContentState() {
        hideAllViews()
        if (mContentView == null) {
            addView(
                LayoutInflater.from(context).inflate(mContentLayout, this, false)
                    .also {
                        mContentView = it
                    }, 0
            )
        } else mContentView!!.visibility = View.VISIBLE
        mContent?.invoke(mContentView!!)
    }

    private fun hideAllViews() {
        for (index in 0..childCount) {
            getChildAt(index).visibility = View.GONE
        }
    }

}