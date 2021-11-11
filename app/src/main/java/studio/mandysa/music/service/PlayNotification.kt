package studio.mandysa.music.service

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.session.PlaybackState
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R

class PlayNotification(private val serviceMedia: MediaPlayService) : Notification.Builder(
    serviceMedia, Media.CHANNEL_ID
) {

    init {
        val manager =
            serviceMedia.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            Media.CHANNEL_ID,
            Media.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.description = Media.CHANNEL_DESCRIPTION
        notificationChannel.enableVibration(false)
        manager.createNotificationChannel(notificationChannel)
    }

    private val mPlayAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        serviceMedia,
        R.drawable.ic_play,
        PlaybackState.STATE_PLAYING
    )
    private val mPauseAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        serviceMedia,
        R.drawable.ic_pause,
        PlaybackState.STATE_PAUSED
    )
    private val mNextAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        serviceMedia,
        R.drawable.ic_skip_next,
        PlaybackState.STATE_SKIPPING_TO_NEXT
    )
    private val mPrevAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        serviceMedia,
        R.drawable.ic_skip_previous,
        PlaybackState.STATE_SKIPPING_TO_PREVIOUS
    )
    private var mIsPlaying = false

    fun setAction(isPlaying: Boolean): PlayNotification {
        mIsPlaying = isPlaying
        setActions(mPrevAction, if (!isPlaying) mPlayAction else mPauseAction, mNextAction)
        return this
    }

    val id: Int
        get() = 1

    override fun build(): Notification {
        serviceMedia.startForeground(1, super.build())
        if (!mIsPlaying) serviceMedia.stopForeground(false)
        return super.build()
    }

    init {
        setShowWhen(false)
        setAction(false)
        setContentIntent(
            PendingIntent.getActivity(
                serviceMedia,
                0,
                Intent(serviceMedia, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        setDeleteIntent(PlayButtonReceiver.buildDeleteIntent(serviceMedia))
        style =
            MediaStyle().setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(serviceMedia.getSessionToken())
        setCategory(Notification.CATEGORY_SERVICE)
        setSmallIcon(R.drawable.ic_music)
    }
}