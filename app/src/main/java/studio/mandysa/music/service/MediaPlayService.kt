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
import android.view.View
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import mandysax.media.DefaultPlayerManager


class MediaPlayService : Service() {

    companion object {
        @Volatile
        @JvmStatic
        var instance: MediaPlayService? = null
    }

    init {
        instance = this
    }

    private val mImageLoader = ImageLoader.getInstance()

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

            mImageLoader.loadImage(it.coverUrl, object : ImageLoadingListener {
                override fun onLoadingStarted(imageUri: String?, view: View?) {
                }

                override fun onLoadingFailed(
                    imageUri: String?,
                    view: View?,
                    failReason: FailReason?
                ) {
                }

                override fun onLoadingComplete(
                    imageUri: String?,
                    view: View?,
                    loadedImage: Bitmap?
                ) {
                    mMediaNotification!!.run {
                        setLargeIcon(
                            Icon.createWithBitmap(
                                loadedImage
                            )
                        )
                        build()
                    }
                }

                override fun onLoadingCancelled(imageUri: String?, view: View?) {
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

    private var mMediaNotification: PlayNotification? = null

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
        mMediaNotification = PlayNotification(this).also {
            it.build()
        }
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        mImageLoader.clearMemoryCache()
        unregisterReceiver(mReceiver)
        session!!.isActive = false
        session = null
        instance = null
    }

    override fun onBind(p0: Intent?): IBinder? = null

}