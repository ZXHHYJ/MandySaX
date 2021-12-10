package studio.mandysa.music.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.mandysa.music.R

class ThreePointsDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemNum = 3
        val length = parent.resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
        view.layoutParams.width = (view.resources.displayMetrics.widthPixels - length) / itemNum
        view.setPadding(length, 0, 0, 0)
    }
}