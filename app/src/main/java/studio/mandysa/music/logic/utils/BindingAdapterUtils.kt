package studio.mandysa.music.logic.utils

import studio.mandysa.jiuwo.adapter.RecyclerAdapter
import java.util.*

object BindingAdapterUtils {
    fun <T> RecyclerAdapter.getModels() =
        mModels as ArrayList<T>
}