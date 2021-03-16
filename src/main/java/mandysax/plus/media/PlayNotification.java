package mandysax.plus.media;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import mandysax.R;
import mandysax.plus.media.PlayNotification;
import mandysax.plus.media.factory.MediaMetadataFactory;
import android.content.Intent;

public final class PlayNotification extends Notification.Builder
{

    private final PlayService service;

    private final NotificationManager manager;

    private final Notification.Action mPlayAction;
    private final Notification.Action mPauseAction;
    private final Notification.Action mNextAction;
    private final Notification.Action mPrevAction;

    private boolean isplaying;

    public PlayNotification(PlayService service)
    {
        super(service, Media.CHANNEL_ID);
        this.service = service;
        manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.getNotificationChannel(Media.CHANNEL_ID);
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
        manager.createNotificationChannel(notificationChannel);     
        setShowWhen(false);
        setWhen(0);
        setAction(false);
        //setActions(mPrevAction, ! false ? mPlayAction: mPauseAction, mNextAction);  
        setDeleteIntent(PlayButtonReceiver.buildDeleteIntent(service));
        setPriority(Notification.PRIORITY_MAX);
        setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2).setMediaSession(service.getSessionToken()));
        setCategory(Notification.CATEGORY_SERVICE);
        setSmallIcon(R.mipmap.ic_music);
        //setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.drawable.cyx));
    }

    public PlayNotification setMediadata(MediaMetadata media)
    {
        if (media == null)
            return this;
        MediaMetadataFactory factory=MediaMetadataFactory.analyze(media);
        setContentTitle(factory.getTitle());
        setContentText(factory.getArtist());
        return this;
    }

    public PlayNotification setAction(boolean isplaying)
    {
        this.isplaying=isplaying;
        setActions(mPrevAction, ! isplaying ? mPlayAction: mPauseAction, mNextAction);  
        //setContentIntent(co.getSession().getController().getSessionActivity());
        return this;
    }

    public int getId()
    {
        return 1;
    }

    @Override
    public Notification build()
    {
        service.startForeground(1, super.build());
        if(!isplaying)
            service.stopForeground(false);
        return null;
    }

}
