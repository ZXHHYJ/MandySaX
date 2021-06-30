package studio.mandysa.music.service;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import java.util.ArrayList;
import mandysax.lifecycle.livedata.LiveData;

public interface PlayManagerImpl
{

	void setCover(Bitmap cover);

	void playMedia(ArrayList<MediaMetadata> mediaList, final int index);

	void play();

	void pause();

	void skipToNext();

	void skipToPrevious();

	boolean isPlaying();

	LiveData<PlaybackState> getPlaybackStateLiveData();

	LiveData<MediaMetadata> getMediaMetadataLiveData();

	LiveData<ArrayList<MediaMetadata>> getPlayListLiveData();

}
