package studio.mandysa.music.logic.utils

import studio.mandysa.music.logic.network.ServiceCreator

/**
 * @author liuxiaoliu66
 */
object ClassUtils {
    fun <T> Class<T>.create(): T {
        return ServiceCreator.create(this)
    }
}