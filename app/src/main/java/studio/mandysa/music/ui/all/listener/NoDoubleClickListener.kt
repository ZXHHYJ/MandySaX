package studio.mandysa.music.ui.all.listener

import android.view.View
import java.util.*

//代码2
abstract class NoDoubleClickListener : View.OnClickListener {
    private var lastClickTime: Long = 0
    abstract fun onNoDoubleClick(view: View?)
    override fun onClick(view: View) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onNoDoubleClick(view)
        }
    }

    companion object {
        const val MIN_CLICK_DELAY_TIME = 1000
    }
}