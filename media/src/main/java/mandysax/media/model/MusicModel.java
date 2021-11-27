package mandysax.media.model;

import java.util.List;

/**
 * @author ZXHHYJ
 */
public interface MusicModel<T extends ArtistModel> {
    String getTitle();

    String getId();

    String getCoverUrl();

    String getUrl();

    List<T> getArtist();
}
