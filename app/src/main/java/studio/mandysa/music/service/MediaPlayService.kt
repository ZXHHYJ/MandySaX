package studio.mandysa.music.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.IBinder
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import mandysax.media.DefaultPlayerManager
import studio.mandysa.music.logic.utils.getFrescoCacheBitmap

class MediaPlayService : Service() {

    companion object {
        @Volatile
        @JvmStatic
        var instance: MediaPlayService? = null
    }

    init {
        instance = this
    }

    private val mInstance = DefaultPlayerManager.getInstance() as DefaultPlayerManager

    private inner class OnPlayStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras!!.getInt(PlayButtonReceiver.PLAY_BACK_STATE)) {
                PlaybackState.STATE_PLAYING -> mInstance.play()
                PlaybackState.STATE_PAUSED -> mInstance.pause()
                PlaybackState.STATE_SKIPPING_TO_NEXT -> mInstance.skipToNext()
                PlaybackState.STATE_SKIPPING_TO_PREVIOUS -> mInstance.skipToPrevious()
                PlaybackState.STATE_STOPPED -> {
                    stopSelf()
                }
            }
        }
    }

    private fun init() {
        mInstance.changeMusicLiveData().observeForever {
            mMediaNotification!!.setContentTitle(it.title)
            mMediaNotification!!.setContentText(it.artist[0].name)
            getFrescoCacheBitmap(
                applicationContext,
                it.coverUrl,
                object : BaseBitmapDataSubscriber() {
                    override fun onNewResultImpl(bitmap: Bitmap?) {
                        mMediaNotification!!.apply {
                            setLargeIcon(
                                Icon.createWithBitmap(
                                    bitmap!!.copy(Bitmap.Config.RGB_565,false)
                                )
                            )
                            build()
                        }
                    }

                    override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {

                    }
                })

        }
        mInstance.pauseLiveData().observeForever {
            upPlaybackState()
            mMediaNotification?.run {
                setAction(!it).build()
            }
        }
        mInstance.playingMusicProgressLiveData().observeForever {
            upPlaybackState()
        }
        mInstance.playingMusicDurationLiveData().observeForever {
            mInstance.changeMusicLiveData().value?.run {
                session?.setMetadata(
                    MediaMetadata.Builder()
                        .putLong(MediaMetadata.METADATA_KEY_DURATION, it.toLong())
                        .putString(MediaMetadata.METADATA_KEY_TITLE, title)
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, artist[0].name).build()
                )
            }
        }
    }

    private fun upPlaybackState() {
        mInstance.playingMusicProgressLiveData().value?.let {
            session?.setPlaybackState(
                PlaybackState.Builder()
                    .setActions(PlaybackState.ACTION_SEEK_TO)
                    .setState(
                        if (mInstance.pauseLiveData().value!!) PlaybackState.STATE_PAUSED else PlaybackState.STATE_PLAYING,
                        it.toLong(),
                        1.0F
                    ).build()
            )
        }
    }

    private var mMediaNotification: MediaNotification? = null

    private var mReceiver: OnPlayStateReceiver? = null

    private var session: MediaSession? = null

    fun getSessionToken(): MediaSession.Token = session!!.sessionToken

    private fun registerBroadcast() {
        mReceiver = OnPlayStateReceiver()
        registerReceiver(mReceiver, IntentFilter(Intent.ACTION_MEDIA_BUTTON))
    }

    override fun onCreate() {
        super.onCreate()
        session = MediaSession(this, packageName).also {
            it.isActive = true
            it.setCallback(object : MediaSession.Callback() {
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    mInstance.seekTo(pos.toInt())
                }
            })
        }
        registerBroadcast()
        mMediaNotification = MediaNotification(this).also {
            it.build()
        }
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        session!!.isActive = false
        session = null
        instance = null
    }

    override fun onBind(p0: Intent?): IBinder? = null

}