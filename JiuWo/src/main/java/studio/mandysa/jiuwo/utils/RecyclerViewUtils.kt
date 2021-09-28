package studio.mandysa.jiuwo.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import studio.mandysa.jiuwo.adapter.RecyclerAdapter

object RecyclerViewUtils {

    fun RecyclerView.linear(
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    ): RecyclerView {
        layoutManager = LinearLayoutManager(context).also {
            it.orientation = orientation
        }
        return this;
    }

    fun RecyclerView.setup(block: RecyclerAdapter.(RecyclerView) -> Unit): RecyclerAdapter {
        val adapter = RecyclerAdapter()
        adapter.block(this)
        this.adapter = adapter
        return adapter
    }

}