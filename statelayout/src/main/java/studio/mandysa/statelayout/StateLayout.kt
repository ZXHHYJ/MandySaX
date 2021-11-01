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
        private var empty: (View.() -> Unit)? = null

        @JvmStatic
        private var loading: (View.() -> Unit)? = null

        @JvmStatic
        private var error: (View.() -> Unit)? = null
    }

    private var mEmpty: (View.() -> Unit)? = empty

    private var mLoading: (View.() -> Unit)? = loading

    private var mError: (View.() -> Unit)? = error

    private val mEmptyLayout: Int

    private val mLoadingLayout: Int

    private val mErrorLayout: Int

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        mEmptyLayout = typedArray.getResourceId(R.styleable.StateLayout_emptyLayout, emptyLayout)
        mLoadingLayout =
            typedArray.getResourceId(R.styleable.StateLayout_loadingLayout, loadingLayout)
        mErrorLayout = typedArray.getResourceId(R.styleable.StateLayout_errorLayout, errorLayout)
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
        val view = showChildAt(0)
        mEmpty?.invoke(view)
    }

    fun showLoadingState() {
        val view = showChildAt(1)
        mLoading?.invoke(view)
    }

    fun showErrorState() {
        val view = showChildAt(2)
        mError?.invoke(view)
    }

    private fun showChildAt(index: Int): View {
        for (index2 in listOf(0, 1, 2)) {
            getChildAt(index).visibility = if (index2 != index) View.GONE else View.VISIBLE
        }
        return getChildAt(index)
    }

}