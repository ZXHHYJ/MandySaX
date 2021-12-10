package studio.mandysa.music

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import mandysax.media.DefaultPlayerManager
import mandysax.media.DefaultPlayerManager.Companion.init
import simon.tuke.Tuke
import studio.mandysa.music.service.MediaPlayService
import studio.mandysa.statelayout.StateLayout

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Tuke.init(this)

        StateLayout.let {
            it.loadingLayout = R.layout.layout_loading
            it.emptyLayout = R.layout.layout_empty
            it.errorLayout = R.layout.layout_error
            it.setRetryId(R.id.cl_error_check)
        }

        val options = DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.empty_iv_bg_color)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()

        //这样自定义设置后就可以管理二级缓存和三级缓存了
        val configuration = ImageLoaderConfiguration.Builder(this)
            .diskCache(UnlimitedDiskCache(cacheDir))
            .memoryCacheSizePercentage(5)//设置占用内存的百分比
            .diskCacheFileCount(100)
            .diskCacheSize(1 * 1024 * 1024)
            .defaultDisplayImageOptions(options)
            .build()

        ImageLoader.getInstance().init(configuration)//初始化完成
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