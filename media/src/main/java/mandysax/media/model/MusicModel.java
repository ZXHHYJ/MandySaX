package mandysax.media.model;

import java.util.List;

public interface MusicModel<T extends ArtistModel> {
    String getTitle();

    String getId();

    String getCoverUrl();

    String getUrl();

    List<T> getArtist();
}
