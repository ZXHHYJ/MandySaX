package studio.mandysa.music.service;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import java.util.ArrayList;
import mandysax.lifecycle.livedata.LiveData;
import mandysax.lifecycle.livedata.MutableLiveData;

public final class PlayService extends Service implements PlayServiceImpl
{

	public final class MediaSessionCallback extends MediaSession.Callback implements AudioManager.OnAudioFocusChangeListener
	{

        @Override
        public void onAudioFocusChange(int p1)
        {
            switch (p1)
            {
                case AudioManager.AUDIOFOCUS_GAIN:
                    onPlay();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    onPause();
                    break;
                default:
            }
        }

		private final MediaPlayer mMediaPlayer=new MediaPlayer();

		private final AudioManager mAudioManager;

        private AudioFocusRequest mAudioFocusRequest;

        private final PlaybackState.Builder mPlaybackState=new PlaybackState.Builder();

        public MediaSessionCallback()
		{
			mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(audioAttributes)
                    .setOnAudioFocusChangeListener(this).build();
            }
        }

		private void setNewState(int state)
		{
			mSession.setPlaybackState(mPlaybackState.setState(state, mMediaPlayer.getCurrentPosition(), 1).build());
		}

		@Override
		public void onSeekTo(long pos)
		{
			super.onSeekTo(pos);
            if (mPrepared)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mMediaPlayer.seekTo(pos, MediaPlayer.SEEK_CLOSEST);
                }
            }
		}

		@Override
		public void onPlay()
		{
			super.onPlay();
            if (mPrepared)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mAudioManager.requestAudioFocus(mAudioFocusRequest);
                }
                mMediaPlayer.start();
                setNewState(PlaybackState.STATE_PLAYING);
            }
		}

		@Override
		public void onPrepare()
		{
			super.onPrepare();
            mMediaPlayer.prepareAsync();
            setNewState(PlaybackState.STATE_CONNECTING);
		}

        private boolean mPrepared;

        @Override
        public void onPrepareFromMediaId(String mediaId, Bundle extras)
        {
            super.onPrepareFromMediaId(mediaId, extras);
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);    
            mMediaPlayer.setOnPreparedListener(p1 -> {
                mPrepared = true;
                onPlay();
            });
            mMediaPlayer.setOnErrorListener((mediaPlayer, i, i1) -> {
                setNewState(PlaybackState.STATE_ERROR);
                return false;
            });
            mMediaPlayer.setOnCompletionListener(mediaPlayer -> onSkipToNext());
            if (mPlayList.getValue() != null)
                try
                {
                    MediaMetadata mediaMetadata=mPlayList.getValue().get(mPosition.getValue());
                    mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mediaMetadata.getString(Media.MEDIA_URI)));
                    onPrepare();
                    mPrepared = false;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    setNewState(PlaybackState.STATE_ERROR);
				}           
        }

		@Override
		public void onSkipToNext()
		{
			super.onSkipToNext();
			setPlaylistPosition(getPlaylistPosition() + 1);
		}

		@Override
		public void onSkipToPrevious()
		{
			super.onSkipToPrevious();
			setPlaylistPosition(getPlaylistPosition() - 1);
		}

		@Override
		public void onPause()
		{
			super.onPause();
            if (mPrepared)
            {               
                setNewState(PlaybackState.STATE_PAUSED);  
                mMediaPlayer.pause();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
                }
            }
		}

		@Override
		public void onStop()
		{
			super.onStop();     
			setNewState(PlaybackState.STATE_STOPPED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
            }
            mMediaPlayer.stop();
			mMediaPlayer.reset();
            mSession.setActive(false);
		}

	}

	@Override
	public void play()
	{
		mController.getTransportControls().play();
	}

	@Override
	public void pause()
	{
		mController.getTransportControls().pause();
	}

	@Override
	public void skipToNext()
	{
		mController.getTransportControls().skipToNext();
	}

	@Override
	public void skipToPrevious()
	{
		mController.getTransportControls().skipToPrevious();
	}

	@Override
	public boolean isPlaying()
    {
        return mIsPlaying;
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

	private class OnPlayStateReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			switch (intent.getExtras().getInt(Media.PLAY_BACK_STATE))
			{
				case PlaybackState.STATE_PLAYING:
					play();
					break;
				case PlaybackState.STATE_PAUSED:
					pause();
					break;
				case PlaybackState.STATE_SKIPPING_TO_NEXT:
					skipToNext();
					break;
				case PlaybackState.STATE_SKIPPING_TO_PREVIOUS:
					skipToPrevious();
					break;
				case PlaybackState.STATE_STOPPED:
					if (mIsPlaying)
						pause();
					break;
			}
		}
	}

    private MediaSession mSession;
    //媒体会话

    private final MutableLiveData<PlayNotification> mPlayNotification=new MutableLiveData<>();
    //媒体通知

    private final MutableLiveData<ArrayList<MediaMetadata>> mPlayList = new MutableLiveData<>();
  	//播放列表

	private final MutableLiveData<PlaybackState> mPlaybackState=new MutableLiveData<>();//播放状态
    //播放状态

	private final MutableLiveData<MediaMetadata> mMediaMetadata =new MutableLiveData<>();//元数据
    //元数据

    private MediaController mController;
    //媒体控制器

    private final MutableLiveData<Integer> mPosition= new MutableLiveData<>(0);
    //正在播放的媒体资源下标

    private boolean mIsPlaying=false;
	//是否正在播放

	void createMediaNonfiction()
    {
        mPlayNotification.setValue(new PlayNotification(this)).build();
        mPlaybackState.observeForever(p1 -> {
            if (p1 != null)
            {
                mPlayNotification.getValue().setAction(mIsPlaying).build();
            }
        });
        registerReceiver(new OnPlayStateReceiver(), new IntentFilter(Intent.ACTION_MEDIA_BUTTON));
    }

	LiveData<PlayNotification> getNotification()
	{
		return mPlayNotification;
	}

    void setPlaylist(ArrayList<MediaMetadata> mediaList, int position)
    {
        mPlayList.setValue(mediaList);
        this.mPosition.setValue(position);
    }

    void setPlaylistPosition(int index)
    {
        this.mPosition.setValue(index);
    }

    int getPlaylistPosition()
    {
        return mPosition.getValue();
    }

    MediaSession.Token getSessionToken()
    {
        return mSession.getSessionToken();
    }   

    LiveData<ArrayList<MediaMetadata>> getMediaList()
    {
        return mPlayList;
    }

    //获取媒体控制器
	MediaController getMediaController()
    {
        return mController;
    }

    //获取媒体会话
	/*MediaSession getSession()
     {
     return mSession;
     }*/

    @Override
    public void onCreate()
    {
        super.onCreate();
        mSession = new MediaSession(this, getPackageName());
        mSession.setActive(true);
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSession.setCallback(new MediaSessionCallback());
		mController = new MediaController(this, mSession.getSessionToken());
        mController.registerCallback(new MediaController.Callback(){

                @Override
                public void onPlaybackStateChanged(PlaybackState state)
                {
                    if (state != null)
                    {
                        mIsPlaying = state.getState() == PlaybackState.STATE_PLAYING;      
                    }
                    mPlaybackState.setValue(state);
                }

                @Override
                public void onMetadataChanged(MediaMetadata media)
                {
                    mMediaMetadata.setValue(media);
                    PlayNotification playNotification=mPlayNotification.getValue();
                    if (playNotification != null)
                    {
                        playNotification.setTitle(media.getString(MediaMetadata.METADATA_KEY_TITLE));
                        playNotification.setArtist(media.getString(MediaMetadata.METADATA_KEY_ARTIST));  
                    }
                    else createMediaNonfiction();
                }
			});
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mSession.release();
    } 

	@Override
    public IBinder onBind(Intent p1)
    {
        return new MediaPlayerServiceIBinder();
    }

	class MediaPlayerServiceIBinder extends Binder
    {       

	    //获取服务
        public PlayService getService()
        {
            return PlayService.this;
        }

    }


}
