package studio.mandysa.music.service.model;

public class DefaultArtist implements ArtistModel {

    private final String mName;

    public DefaultArtist(String name) {
        this.mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }

}
