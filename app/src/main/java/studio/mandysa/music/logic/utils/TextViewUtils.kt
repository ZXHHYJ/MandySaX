package studio.mandysa.music.logic.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import studio.mandysa.music.R
import java.util.regex.Pattern

/**
 * @author liuxiaoliu66
 */
fun TextView.markByColor(keyword: String?) {
    if (keyword == null) return
    val s = SpannableString(text)
    val m = Pattern.compile(keyword).matcher(s)
    while (m.find()) {
        s.setSpan(
            ForegroundColorSpan(context.getColor(R.color.main)),
            m.start(),
            m.end(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    text = s
}