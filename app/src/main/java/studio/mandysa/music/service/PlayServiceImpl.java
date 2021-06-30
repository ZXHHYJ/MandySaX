package studio.mandysa.music.service;

import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import java.util.ArrayList;
import mandysax.lifecycle.livedata.LiveData;

public interface PlayServiceImpl
{

	void play();

	void pause();

	void skipToNext();

	void skipToPrevious();
	
	LiveData<PlaybackState> getPlaybackStateLiveData();
	
	LiveData<MediaMetadata> getMediaMetadataLiveData();
	
	LiveData<ArrayList<MediaMetadata>> getPlayListLiveData();
	
	boolean isPlaying();
}
