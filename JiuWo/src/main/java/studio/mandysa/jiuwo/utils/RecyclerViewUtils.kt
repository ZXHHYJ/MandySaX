package studio.mandysa.jiuwo.utils

import android.annotation.SuppressLint
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
        return this;
    }

    fun RecyclerView.staggered(
        spanCount: Int = 1,
        @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
    ): RecyclerView {
        layoutManager = StaggeredGridLayoutManager(spanCount, orientation)
        return this;
    }

    fun RecyclerView.setup(block: RecyclerAdapter.(RecyclerView) -> Unit): RecyclerAdapter {
        val adapter = RecyclerAdapter()
        adapter.block(this)
        this.adapter = adapter
        return adapter
    }

    fun RecyclerView.addModel(list: List<Any>) {
        val adapter = adapter as RecyclerAdapter
        adapter.addModels(list)
    }

    var RecyclerView.models
        get() = (adapter as RecyclerAdapter).mModels as List<Any?>
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            val adapter = adapter as RecyclerAdapter
            adapter.mModels = value as MutableList<Any?>
            adapter.notifyDataSetChanged()
        }

}