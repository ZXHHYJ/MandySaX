package studio.mandysa.music.logic.Model;

import java.util.List;

import mandysax.anna2.annotation.Value;

public class PlaylistInfoModel {
    @Value("name")
    public String name;

    @Value("description")
    public String description;

    @Value("coverImgUrl")
    public String coverImgUrl;

    @Value("trackIds")
    public List<SongList> songList;

    public static class SongList {

        @Value("id")
        public String id;

    }
}
