package studio.mandysa.music.logic.utils

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.showInput() {
    requestFocus()
    val imm = imm()
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}


fun EditText.hideInput() {
    val imm = imm()
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun EditText.imm(): InputMethodManager =
    context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager