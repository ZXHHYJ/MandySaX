package mandysax.plus.media;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;

public class PlayButtonReceiver
{

    public static Notification.Action buildMediaButtonAction(Context context, int icon, int state)
    {
        return
            new Notification.Action(
            icon,
            null
            ,
            PendingIntent.getBroadcast(context, state, new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(Media.PLAY_BACK_STATE, state), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    public static PendingIntent buildDeleteIntent(Context context)
    {
        return PendingIntent.getBroadcast(context, PlaybackState.STATE_STOPPED, new Intent(Media.CHANNEL_NAME).putExtra(Media.PLAY_BACK_STATE, PlaybackState.STATE_STOPPED), PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
