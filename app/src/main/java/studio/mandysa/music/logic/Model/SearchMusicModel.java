package studio.mandysa.music.logic.Model;

import android.media.MediaMetadata;

import mandysax.anna2.annotation.Array;
import mandysax.anna2.annotation.Value;
import mandysax.anna2.annotation.Path;
import studio.mandysa.music.service.Media;
import studio.mandysa.music.logic.Network.Url;

@Array
public class SearchMusicModel {
    @Value("name")
    public String name;

    @Value("id")
    public int id;

    @Value("duration")
    public long duration;

    @Value("name")
    @Path("artists")
    public String artistsName;

    public String picUrl;

    private MediaMetadata mBuild;

    public MediaMetadata build() {
        if (mBuild != null) return mBuild;
        MediaMetadata.Builder build = new MediaMetadata.Builder();
        build.putString(Media.ARTIST, artistsName);
        build.putString(Media.MEDIA_ID, id + "");
        build.putString(Media.TITLE, name);
        build.putString(Media.MEDIA_URI, Url.MUSIC_URL + id);
        build.putLong(Media.DURATION, duration);
        return mBuild = build.build();
    }

}
