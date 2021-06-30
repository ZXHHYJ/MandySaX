package studio.mandysa.music.service.model;

import java.util.ArrayList;

public interface MusicModel {
    String getTitle();

    String getCoverUrl();

    String getUrl();

    ArrayList<DefaultArtist> getArtist();
}
