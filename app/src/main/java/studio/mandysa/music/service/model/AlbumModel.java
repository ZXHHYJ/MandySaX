package studio.mandysa.music.service.model;

import java.util.ArrayList;

public interface AlbumModel {
    void add(MusicModel musicModel);

    void remove(MusicModel musicModel);

    int size();

    MusicModel get(int index);

    ArrayList<ArtistModel> getArtist();
}
