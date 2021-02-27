package mandysax.plus.media.factory;
import android.graphics.Bitmap;
import android.media.MediaMetadata;

public class MediaMetadataFactory
{
    private final MediaMetadata media;

    private MediaMetadataFactory(MediaMetadata media)
    {
        this.media = media;
    }

    public String getTitle()
    {
        return media.getString(MediaMetadata.METADATA_KEY_TITLE);
    }

    public String getArtist()
    {
        return media.getString(MediaMetadata.METADATA_KEY_ARTIST);
    }

    public String getId()
    {
        return media.getString(MediaMetadata.METADATA_KEY_MEDIA_ID);
    }

    public long getDuration()
    {
        return media.getLong(MediaMetadata.METADATA_KEY_DURATION);
    }

    public Bitmap getAlbumBitmap()
    {
        return media.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART);
    }

    public String getAlbumTitle()
    {
        return media.getString(MediaMetadata.METADATA_KEY_ALBUM);
    }

    public String getAlbumArtist()
    {
        return media.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST);
    }

    public static MediaMetadataFactory analyze(MediaMetadata media)
    {
        return new MediaMetadataFactory(media);
    } 

    public static MediaMetadata create
    (
        String title,
        String id,  
        String albumTitle,
        String artist
    )
    {
        return
            new MediaMetadata.Builder()
            .putLong(MediaMetadata.METADATA_KEY_DURATION,99999)
            .putString(MediaMetadata.METADATA_KEY_TITLE, title)
            .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, id)   
            .putString(MediaMetadata.METADATA_KEY_ALBUM, albumTitle)
            .putString(MediaMetadata.METADATA_KEY_ARTIST, artist).build();
    }
}
