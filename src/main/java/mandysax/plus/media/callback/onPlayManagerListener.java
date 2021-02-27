package mandysax.plus.media.callback;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import java.util.List;

public abstract interface onPlayManagerListener
{

    public abstract void onMetadataChanged(MediaMetadata media)

    public abstract void onPlaybackStateChanged(PlaybackState playbackState)

    public abstract void onPlayModeChanged(int mode)

    public abstract void onPlayMetadataListChanged(List<MediaMetadata> mediaList)

}
