package studio.mandysa.jiuwo.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import studio.mandysa.jiuwo.adapter.RecyclerAdapter

object RecyclerViewUtils {

    fun RecyclerView.linear(
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    ): RecyclerView {
        layoutManager = LinearLayoutManager(context).also {
            it.orientation = orientation
        }
        return this
    }

    fun RecyclerView.staggered(
        spanCount: Int = 1,
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
    ): RecyclerView {
        layoutManager = StaggeredGridLayoutManager(spanCount, orientation)
        return this
    }

    fun RecyclerView.setup(block: RecyclerAdapter.(RecyclerView) -> Unit): RecyclerAdapter {
        val adapter = RecyclerAdapter()
        adapter.block(this)
        this.adapter = adapter
        return adapter
    }

    fun RecyclerView.addHeader(model: Any) {
        recyclerAdapter.addHeader(model)
    }

    fun RecyclerView.addModels(list: List<Any>) {
        recyclerAdapter.addModels(list)
    }

    val RecyclerView.recyclerAdapter
        get() = (adapter as RecyclerAdapter)

}