package studio.mandysa.music.logic.utils

import mandysax.media.DefaultPlayerManager

object DefaultPlayManagerUtils {
    fun getInstance(): DefaultPlayerManager {
        return DefaultPlayerManager.getInstance()!!
    }
}