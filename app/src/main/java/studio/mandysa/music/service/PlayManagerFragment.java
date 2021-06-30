package studio.mandysa.music.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.os.IBinder;

import java.util.ArrayList;

import mandysax.fragment.Fragment;
import mandysax.lifecycle.livedata.LiveData;
import mandysax.lifecycle.livedata.MutableLiveData;
import mandysax.lifecycle.livedata.Observer;

final class PlayManagerFragment extends Fragment implements PlayManagerImpl,Observer<PlayService>,ServiceConnection
{

	@Override
	public void setCover(Bitmap cover)
	{
		if (mMediaMetadata.getValue() != null)
		{
			mPlayService.getValue().getNotification().getValue().setCover(cover);
		}
	}

	@Override
	public void playMedia(final ArrayList<MediaMetadata> mediaList, final int index)
	{
		if (mPlayService.getValue() == null)
			mPlayService.lazy(p1 -> playMedia(mediaList, index));
		else
		{
            PlayService playService=mPlayService.getValue();
			playService.setPlaylist(mediaList, index);
			//playService.getSession().setMetadata(mediaList.get(index));
			playService.getMediaController().getTransportControls().playFromMediaId(mediaList.get(index).getString(MediaMetadata.METADATA_KEY_MEDIA_ID), null);                         
		}
	}

	@Override
	public void play()
	{
		if (mPlayService.getValue() == null)
			mPlayService.lazy(p1 -> play());
		else mPlayService.getValue().play();
	}

	@Override
	public void pause()
	{
		if (mPlayService.getValue() == null)
			mPlayService.lazy(p1 -> pause());
		else mPlayService.getValue().pause();
	}

	@Override
	public void skipToNext()
	{
		if (mPlayService.getValue() == null)
			mPlayService.lazy(p1 -> skipToNext());
		else mPlayService.getValue().skipToNext();
	}

	@Override
	public void skipToPrevious()
	{
		if (mPlayService.getValue() == null)
			mPlayService.lazy(p1 -> skipToPrevious());
		else mPlayService.getValue().skipToPrevious();
	}

	@Override
	public boolean isPlaying()
	{
		if (mPlayService.getValue() != null)
            return mPlayService.getValue().isPlaying();
        return false;
	}

	@Override
	public void onServiceConnected(ComponentName p1, IBinder p2)
	{
        PlayService playService=((PlayService.MediaPlayerServiceIBinder)p2).getService();
		mPlayService.postValue(playService);
	}

	@Override
	public void onServiceDisconnected(ComponentName p1)
	{
		mPlayService.setValue(null);
	}

	@Override
	public void onChanged(PlayService p1)
	{
		p1.getNotification().lazy(p11 -> p11.setActivity(getActivity()));
		p1.getMediaList().observeForever(mPlayList::postValue);
		p1.getMediaMetadataLiveData().observeForever(mMediaMetadata::setValue);
		p1.getPlaybackStateLiveData().observeForever(mPlaybackState::setValue);
		mMediaMetadata.postValue(p1.getMediaMetadataLiveData().getValue());
		mPlaybackState.postValue(p1.getPlaybackStateLiveData().getValue());
	}

	@Override
	public LiveData<PlaybackState> getPlaybackStateLiveData()
	{
		return mPlaybackState;
	}

	@Override
	public LiveData<MediaMetadata> getMediaMetadataLiveData()
	{
		return mMediaMetadata;
	}

	@Override
	public LiveData<ArrayList<MediaMetadata>> getPlayListLiveData()
	{
		return mPlayList;
	}

	private final MutableLiveData<PlayService> mPlayService=new MutableLiveData<PlayService>().lazy(this);//后台服务

	private final MutableLiveData<PlaybackState> mPlaybackState=new MutableLiveData<>();//播放状态

	private final MutableLiveData<MediaMetadata> mMediaMetadata =new MutableLiveData<>();//元数据

	private final MutableLiveData<ArrayList<MediaMetadata>> mPlayList=new MutableLiveData<>();//播放列表

	@Override
	protected void onAttach(Context context)
	{
		super.onAttach(context);
   		if (mPlayService.getValue() == null)context.getApplicationContext().bindService(new Intent(context.getApplicationContext(), PlayService.class), this, Context.BIND_AUTO_CREATE);
	}

}
