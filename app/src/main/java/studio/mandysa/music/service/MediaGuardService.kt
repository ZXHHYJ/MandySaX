package studio.mandysa.music.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import mandysax.media.DefaultPlayerManager.Companion.getInstance
import studio.mandysa.music.service.MediaPlayService.Companion.instance

class MediaGuardService : Service() {
    override fun onCreate() {
        super.onCreate()
        getInstance()!!.apply {
            pauseLiveData().observeForever {
                if (instance == null) {
                    synchronized(MediaPlayService::class.java) {
                        startService(
                            Intent(
                                applicationContext,
                                MediaPlayService::class.java
                            )
                        )
                    }
                }
            }
            changeMusicLiveData()
                .observeForever {
                    if (instance == null) {
                        synchronized(MediaPlayService::class.java) {
                            startService(
                                Intent(
                                    applicationContext,
                                    MediaPlayService::class.java
                                )
                            )
                        }
                    }
                }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}