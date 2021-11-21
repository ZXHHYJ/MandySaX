package studio.mandysa.music.ui.search

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.mandysa.music.R

class DoubleItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val length = parent.resources.getDimensionPixelOffset(R.dimen.double_item_margin)
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = length
            outRect.right = length / 2
        } else {
            outRect.left = length / 2
            outRect.right = length
        }
        //super.getItemOffsets(outRect, view, parent, state)
    }
}