package studio.mandysa.music.logic.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import studio.mandysa.music.R

class AlbumItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val length =
            parent.resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
        view.layoutParams.width =
            parent.resources.getDimensionPixelOffset(R.dimen.album_width) + length / 2
        view.setPadding(length, 0, 0, 0)
    }
}