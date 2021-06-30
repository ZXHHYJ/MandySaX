package studio.mandysa.music.logic.Model;

import android.media.MediaMetadata;

import mandysax.anna2.annotation.Array;
import mandysax.anna2.annotation.Value;
import mandysax.anna2.annotation.Path;
import studio.mandysa.music.service.Media;
import studio.mandysa.music.logic.Network.Url;

@Array
public class NewSongModel {
    @Value("id")
    public int id;

    @Value("name")
    public String name;

    @Value("picUrl")
    public String picUrl;

    @Value("name")
    @Path("song/artists")
    public String artistsName;

    private MediaMetadata mBuild;

    public MediaMetadata build() {
        if (mBuild != null) return mBuild;
        MediaMetadata.Builder build = new MediaMetadata.Builder();
        build.putString(Media.ARTIST, artistsName);
        build.putString(Media.MEDIA_ID, id + "");
        build.putString(Media.TITLE, name);
        build.putString(Media.MEDIA_URI, Url.MUSIC_URL + id);
        return mBuild = build.build();
    }
}
