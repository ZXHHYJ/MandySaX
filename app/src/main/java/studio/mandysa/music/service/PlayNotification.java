package studio.mandysa.music.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.session.PlaybackState;

import mandysax.R;
import mandysax.fragment.FragmentActivity;

final class PlayNotification extends Notification.Builder {

    void setCover(Bitmap bitmap) {
        setLargeIcon(bitmap);
        build();
    }

    void setActivity(FragmentActivity activity) {
        if (activity != null)
            setContentIntent(PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    void setTitle(String title) {
        setContentTitle(title);
    }

    void setArtist(String artist) {
        setContentText(artist);
    }

    private final PlayService mPlayService;

    private final Notification.Action mPlayAction;
    private final Notification.Action mPauseAction;
    private final Notification.Action mNextAction;
    private final Notification.Action mPrevAction;

    private boolean mIsPlaying;

    PlayNotification(PlayService service) {
        super(service, Media.CHANNEL_ID);
        this.mPlayService = service;
        NotificationManager mManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.getNotificationChannel(Media.CHANNEL_ID);
        mPlayAction = PlayButtonReceiver.buildMediaButtonAction(service, R.mipmap.ic_play, PlaybackState.STATE_PLAYING);
        mPauseAction =
                PlayButtonReceiver.buildMediaButtonAction(service, R.mipmap.ic_pause, PlaybackState.STATE_PAUSED);
        mNextAction =
                PlayButtonReceiver.buildMediaButtonAction(service, R.mipmap.ic_skip_next, PlaybackState.STATE_SKIPPING_TO_NEXT);
        mPrevAction =
                PlayButtonReceiver.buildMediaButtonAction(service, R.mipmap.ic_skip_previous, PlaybackState.STATE_SKIPPING_TO_PREVIOUS);
        NotificationChannel notificationChannel = new NotificationChannel(Media.CHANNEL_ID, Intent.ACTION_MEDIA_BUTTON, NotificationManager.IMPORTANCE_LOW);
        notificationChannel.setDescription(Media.CHANNEL_DESCRIPTION);
        notificationChannel.enableVibration(false);
        mManager.createNotificationChannel(notificationChannel);
        setShowWhen(false);
        setWhen(0);
        setAction(false);
        setDeleteIntent(PlayButtonReceiver.buildDeleteIntent(service));
        setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2).setMediaSession(service.getSessionToken()));
        setCategory(Notification.CATEGORY_SERVICE);
        setSmallIcon(R.mipmap.ic_music);
    }

    PlayNotification setAction(boolean isPlaying) {
        this.mIsPlaying = isPlaying;
        setActions(mPrevAction, !isPlaying ? mPlayAction : mPauseAction, mNextAction);
        return this;
    }

    @Override
    public Notification build() {
        mPlayService.startForeground(1, super.build());
        if (!mIsPlaying)
            mPlayService.stopForeground(false);
        return null;
    }

}
