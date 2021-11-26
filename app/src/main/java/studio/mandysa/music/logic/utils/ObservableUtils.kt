package studio.mandysa.music.logic.utils

import mandysax.anna2.callback.Callback
import mandysax.anna2.observable.Observable
import mandysax.lifecycle.Lifecycle
import mandysax.lifecycle.LifecycleObserver

inline fun <reified T> Observable<T>.set(lifecycle: Lifecycle, callback: Callback<T>) {
    set(callback)
    lifecycle.addObserver(object : LifecycleObserver {
        override fun Observer(state: Lifecycle.Event?) {
            if (state == Lifecycle.Event.ON_DESTROY) {
                cancel()
                lifecycle.removeObserver(this)
            }
        }

    })
}