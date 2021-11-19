package studio.mandysa.music.logic.utils

import studio.mandysa.jiuwo.adapter.RecyclerAdapter
import java.util.*

fun <T> RecyclerAdapter.getModels() =
    models as ArrayList<T>