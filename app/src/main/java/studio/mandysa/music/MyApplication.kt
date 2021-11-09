package studio.mandysa.music

import android.app.Application
import android.content.Intent
import com.facebook.drawee.backends.pipeline.Fresco
import mandysax.media.DefaultPlayerManager
import mandysax.media.DefaultPlayerManager.Companion.init
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.statelayout.StateLayout

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StateLayout.apply {
            loadingLayout = R.layout.layout_loading
            emptyLayout = R.layout.layout_empty
            errorLayout = R.layout.layout_error
        }
        Fresco.initialize(this) //初始化图片加载库
        init(this) //初始化播放管理器
        DefaultPlayerManager.getInstance()!!.apply {
            pauseLiveData()
                .observeForever {
                    if (it == false)
                        startPlayerService()
                }
            changeMusicLiveData()
                .observeForever {
                    if (it != null)
                        startPlayerService()
                }
        }
    }

    private fun startPlayerService() {
        if (MediaPlayService.instance == null) {
            startService(
                Intent(
                    this@MyApplication,
                    MediaPlayService::class.java
                )
            )
        }
    }
}