package mandysax.plus.media;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Binder;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.List;
import mandysax.plus.lifecycle.livedata.LiveData;
import mandysax.plus.lifecycle.livedata.MutableLiveData;
import mandysax.plus.lifecycle.livedata.Observer;
import mandysax.plus.media.callback.onMediaConfigurationCallback;
import mandysax.plus.media.factory.MediaMetadataFactory;

public final class PlayService extends Service
{

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
					if (isPlaying)
						pause();
					//stopSelf();
					break;
			}
		}
	}

	private void registerBroadcast()
	{
		registerReceiver(new OnPlayStateReceiver(), new IntentFilter(Intent.ACTION_MEDIA_BUTTON));
	}

    private MediaSession session;

    private PlayNotification notification;

    private List<MediaMetadata.Builder> mediaList=new ArrayList<>();
	//播放列表

    private final MutableLiveData<MediaController> _controller = new MutableLiveData<MediaController>();
    //媒体控制器

    private final LiveData<MediaController> controller=_controller;
    //媒体控制器(暴露)

    private final MutableLiveData<onMediaConfigurationCallback> cCallback= new MutableLiveData<onMediaConfigurationCallback>();
    //播放配置

    private final MutableLiveData<Integer> index=new MutableLiveData<Integer>(0);
    //正在播放的媒体资源下标

    private boolean isPlaying=false;
	//是否正在播放

    private void createMediaNotfication()
    {
        if (notification != null)
            return;
        notification = new PlayNotification(this);
        notification.build();
        registerBroadcast();
    }

    public void play()
    {   
        getMediaController().apply(new Observer<MediaController>(){
                @Override
                public void onChanged(MediaController p1)
                {
                    p1.getTransportControls().play();              
                }
            });
    }

    public void pause()
    {
        getMediaController().apply(new Observer<MediaController>(){
                @Override
                public void onChanged(MediaController p1)
                {
					p1.getTransportControls().pause();
                }
            });
    }

    public void skipToNext()
    {
        getMediaController().apply(new Observer<MediaController>(){

                @Override
                public void onChanged(MediaController p1)
                {
                    p1.getTransportControls().skipToNext();
                }                        
            });
    }

    public void skipToPrevious()
    {
        getMediaController().apply(new Observer<MediaController>(){

                @Override
                public void onChanged(MediaController p1)
                {
                    p1.getTransportControls().skipToPrevious();
                }                        
            });
    }   

    public void setMediaList(List<MediaMetadata.Builder> mediaList)
    {
        this.mediaList = mediaList;
    }

    public void setOnMediaConfigurationCallback(onMediaConfigurationCallback callback)
    {
        if (cCallback.getValue() == null)
            cCallback.setValue(callback);
    }

    public void setMediaIndex(int index)
    {
        this.index.setValue(index);
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }

    public int getMediaIndex()
    {
        return index.getValue();
    }

    public List<MediaMetadata.Builder> getMediaList()
    {
        return mediaList;
    }

    public MediaSession.Token getSessionToken()
    {
        return session.getSessionToken();
    }    

    public LiveData<onMediaConfigurationCallback> getOnMediaConfigurationCallback()
    {
        return cCallback;
    }

    public LiveData<MediaController> getMediaController()
    {
        return controller;
    }

    public MediaSession getSession()
    {
        return session;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        session = new MediaSession(this, getClass().getCanonicalName());
        session.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        session.setCallback(new MediaSessionCompatCallback(this));
        _controller.setValue(new MediaController(this, session.getSessionToken()));
        _controller.getValue().registerCallback(new MediaController.Callback(){

                @Override
                public void onPlaybackStateChanged(PlaybackState playbackState)
                {
                    if (playbackState != null)
                    {
                        isPlaying = playbackState.getState() == PlaybackState.STATE_PLAYING;      
                        session.setActive(isPlaying);
                        notification.setAction(isPlaying).build();
                    }
                }

                @Override
                public void onMetadataChanged(MediaMetadata media)
                {
                    if (media == null)return;
                    if (notification != null)
                        notification.setMediadata(media).build();
                    else createMediaNotfication();
                }
            });
        index.observeForever(new Observer<Integer>(){
                @Override
                public void onChanged(final Integer p1)
                {
                    if (getMediaList().size() >= p1 + 1 && p1 >= 0)
                    {
                        getMediaController().apply(new Observer<MediaController>(){
                                @Override
                                public void onChanged(MediaController p2)
                                {
                                    getSession().setMetadata(getMediaList().get(p1).build());
                                    p2.getTransportControls().playFromMediaId(MediaMetadataFactory.analyze(getMediaList().get(p1).build()).getId(), null);
                                }
                            });
                    }
                }
            });


    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        session.release();//释放session
    } 

	@Override
    public IBinder onBind(Intent p1)
    {
        return  new MediaPlayerServiceIBinder();//返回bind以和activity进行交互
    }

	public class MediaPlayerServiceIBinder extends Binder//ibinder是服务的子类
    {       

	    //获取服务
        public PlayService getService()
        {
            return PlayService.this;
        }

    }


}
