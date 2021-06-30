package studio.mandysa.music.service.model;

import java.util.ArrayList;

public class DefaultAlbum implements AlbumModel {

    private final ArrayList<ArtistModel> mArtistModel = new ArrayList<>();

    private final ArrayList<MusicModel> mMusicList = new ArrayList<>();

    @Override
    public void add(MusicModel musicModel) {
        mMusicList.add(musicModel);
    }

    @Override
    public void remove(MusicModel musicModel) {
        mMusicList.remove(musicModel);
    }

    @Override
    public int size() {
        return mMusicList.size();
    }

    @Override
    public MusicModel get(int index) {
        return mMusicList.get(index);
    }

    @Override
    public ArrayList<ArtistModel> getArtist() {
        return mArtistModel;
    }

}
