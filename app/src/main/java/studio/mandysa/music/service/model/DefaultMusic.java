package studio.mandysa.music.service.model;

import java.util.ArrayList;

public class DefaultMusic implements MusicModel {

    private String mTitle;

    private String mCoverUrl;

    private String mUrl;

    private final ArrayList<DefaultArtist> mArtistList = new ArrayList<>();

    public DefaultMusic setTitle(String title) {
        mTitle = title;
        return this;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public DefaultMusic setCoverUrl(String url) {
        mCoverUrl = url;
        return this;
    }

    @Override
    public String getCoverUrl() {
        return mCoverUrl;
    }

    public DefaultMusic setUrl(String url) {
        mUrl = url;
        return this;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public ArrayList<DefaultArtist> getArtist() {
        return mArtistList;
    }

}
