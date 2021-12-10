package studio.mandysa.music.logic.utils

import android.widget.Toast
import mandysax.fragment.Fragment

fun Fragment.toast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}