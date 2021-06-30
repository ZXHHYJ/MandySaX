package studio.mandysa.music.ui.viewmodel;

import android.media.MediaMetadata;

import java.util.ArrayList;

import mandysax.lifecycle.ViewModel;
import studio.mandysa.music.service.Media;
import simon.tuke.Tuke;
import studio.mandysa.music.logic.Network.Url;

public class DatabaseViewModel extends ViewModel {

    public static final String KEY = "database_key";

    private final ArrayList<MusicModel> mDatabaseList;

    public DatabaseViewModel() {
        mDatabaseList = Tuke.get(KEY, new ArrayList<>());
    }

    public boolean isHasMusicId(int id) {
        for (MusicModel musicModel : mDatabaseList) {
            if (musicModel.id == id)
                return true;
        }
        return false;
    }

    public void addMusic(studio.mandysa.music.logic.Model.MusicModel musicModel) {
        if (isHasMusicId(musicModel.id)) return;
        MusicModel musicModel1 = new MusicModel();
        musicModel1.id = musicModel.id;
        musicModel1.artistsName = musicModel.artistsName;
        musicModel1.name = musicModel.name;
        musicModel1.picUrl = musicModel.picUrl;
        mDatabaseList.add(musicModel1);
    }

    public void removeMusic(MusicModel musicModel) {
        mDatabaseList.remove(musicModel);
    }

    public void removeMusic(int index) {
        mDatabaseList.remove(index);
    }

    public static final class MusicModel {
        public int id;

        public String name;

        public String picUrl;

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
}
