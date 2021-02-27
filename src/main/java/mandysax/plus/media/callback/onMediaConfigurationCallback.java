package mandysax.plus.media.callback;
import android.media.MediaMetadata;

public abstract interface onMediaConfigurationCallback
{
    public abstract String getUrl(String mediaId)
    
    public abstract String getCoverUrl(String mediaId)
    
}
