package studio.mandysa.music.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.IBinder
import mandysax.media.DefaultPlayerManager
import okhttp3.*
import java.io.IOException
import java.io.InputStream


class MediaPlayService : Service() {

    private val mInstance = DefaultPlayerManager.getInstance() as DefaultPlayerManager

    private inner class OnPlayStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras!!.getInt(Media.PLAY_BACK_STATE)) {
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

    init {
        mInstance.changeMusicLiveData().observeForever {
            if (mMediaNotification == null) {
                createMediaNotification()
            }
            mMediaNotification!!.setContentTitle(it.title)
            mMediaNotification!!.setContentText(it.artist[0].name)

            val request = Request.Builder()
                .url(it.coverUrl)
                .build()
            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.apply {
                        val inputStream: InputStream = this.byteStream() //得到图片的流
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                        mMediaNotification!!.run {
                            setLargeIcon(
                                Icon.createWithBitmap(
                                    bitmap
                                )
                            )
                            build()
                        }
                    }
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

    //是否正在播放
    private fun createMediaNotification() {
        if (mMediaNotification != null) return
        registerBroadcast()
        mMediaNotification = PlayNotification(this).also {
            it.build()
        }
    }

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
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        session!!.isActive = false
        session = null
    }

    override fun onBind(p0: Intent?): IBinder? = null

}