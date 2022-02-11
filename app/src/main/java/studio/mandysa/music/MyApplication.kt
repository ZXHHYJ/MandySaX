package studio.mandysa.music

import android.app.Application
import android.content.Intent
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.mmkv.MMKV
import mandysax.media.DefaultPlayerManager
import mandysax.media.DefaultPlayerManager.Companion.init
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.statelayout.StateLayout

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        Fresco.initialize(this)

        StateLayout.let {
            it.loadingLayout = R.layout.layout_loading
            it.emptyLayout = R.layout.layout_empty
            it.errorLayout = R.layout.layout_error
            it.setRetryId(R.id.cl_error_check)
        }

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