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

class MediaNotification(private val mediaPlayService: MediaPlayService) : Notification.Builder(
    mediaPlayService, mediaPlayService.getString(R.string.CHANNEL_ID)
) {

    init {
        val manager =
            mediaPlayService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            mediaPlayService.getString(R.string.CHANNEL_ID),
            mediaPlayService.getString(R.string.CHANNEL_NAME),
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.description = mediaPlayService.getString(R.string.CHANNEL_DESCRIPTION)
        notificationChannel.enableVibration(false)
        manager.createNotificationChannel(notificationChannel)
    }

    private val mPlayAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        mediaPlayService,
        R.drawable.ic_play,
        PlaybackState.STATE_PLAYING
    )
    private val mPauseAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        mediaPlayService,
        R.drawable.ic_pause,
        PlaybackState.STATE_PAUSED
    )
    private val mNextAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        mediaPlayService,
        R.drawable.ic_skip_next,
        PlaybackState.STATE_SKIPPING_TO_NEXT
    )
    private val mPrevAction: Notification.Action = PlayButtonReceiver.buildMediaButtonAction(
        mediaPlayService,
        R.drawable.ic_skip_previous,
        PlaybackState.STATE_SKIPPING_TO_PREVIOUS
    )
    private var mIsPlaying = false

    fun setAction(isPlaying: Boolean): MediaNotification {
        mIsPlaying = isPlaying
        setActions(mPrevAction, if (!isPlaying) mPlayAction else mPauseAction, mNextAction)
        return this
    }

    val id: Int
        get() = 1

    override fun build(): Notification {
        mediaPlayService.startForeground(id, super.build())
        if (!mIsPlaying) mediaPlayService.stopForeground(false)
        return super.build()
    }

    init {
        setShowWhen(false)
        setAction(false)
        setContentIntent(
            PendingIntent.getActivity(
                mediaPlayService,
                0,
                Intent(mediaPlayService, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        setDeleteIntent(PlayButtonReceiver.buildDeleteIntent(mediaPlayService))
        style =
            MediaStyle().setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(mediaPlayService.getSessionToken())
        setCategory(Notification.CATEGORY_SERVICE)
        setSmallIcon(R.drawable.ic_round_music_note_24)
    }
}