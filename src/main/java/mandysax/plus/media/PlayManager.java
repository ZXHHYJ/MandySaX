package mandysax.plus.media;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.IBinder;
import java.util.List;
import mandysax.plus.fragment.Fragment;
import mandysax.plus.fragment.FragmentActivity;
import mandysax.plus.lifecycle.livedata.MutableLiveData;
import mandysax.plus.lifecycle.livedata.Observer;
import mandysax.plus.media.PlayService;
import mandysax.plus.media.callback.onMediaConfigurationCallback;
import mandysax.plus.media.callback.onPlayManagerListener;
import mandysax.plus.media.factory.MediaMetadataFactory;

public final class PlayManager implements PlayManagerImpl
{

    @Override
    public PlayManager setOnPlayManagerListener(onPlayManagerListener listener)
    {
        playManagerFragment.onPlayManagerListener.setValue(listener);
        return this;
    }

	public static class PlayManagerFragment extends Fragment implements PlayManagerImpl
	{

        @Override
        public PlayManager setOnMediaConfigurationCallback(onMediaConfigurationCallback callback)
        {
            return null;
        }

        private MutableLiveData<PlayService> service=new MutableLiveData<PlayService>().apply(new Observer<PlayService>(){

                @Override
                public void onChanged(final PlayService p1)
                {
                    onPlayManagerListener.apply(new Observer<onPlayManagerListener>(){

                            @Override
                            public void onChanged(final onPlayManagerListener p2)
                            {
                                p1.getSession().getController().registerCallback(new MediaController.Callback(){
                                        @Override
                                        public void onPlaybackStateChanged(PlaybackState state)
                                        {
                                            p2.onPlaybackStateChanged(state);
                                        }

                                        @Override
                                        public void onMetadataChanged(MediaMetadata media)
                                        {
                                            p2.onMetadataChanged(media);
                                        }
                                    });
									/*System.out.println(p2);
									System.out.println(p1.getSession());*/
                              p2.onPlaybackStateChanged(p1.getSession().getController().getPlaybackState());
                              p2.onMetadataChanged(p1.getSession().getController().getMetadata());
                            }           
                        });
                }


            });

        private final MutableLiveData<onPlayManagerListener> onPlayManagerListener = new MutableLiveData<>();

        @Override
        public PlayManager setOnPlayManagerListener(onPlayManagerListener listener)
        {
            onPlayManagerListener.setValue(listener);
            return null;
        }

        @Override
        protected void onActivityCreated(Bundle savedInstanceState)
        {
            super.onActivityCreated(savedInstanceState);
            getActivity().getApplicationContext().bindService(new Intent(getActivity(), PlayService.class), new ServiceConnection(){
                    @Override
                    public void onServiceConnected(ComponentName p1, IBinder p2)
                    {
                        service.postValue(((PlayService.MediaPlayerServiceIBinder)p2).getService());
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName p1)
                    {
                        service.postValue(null);
                    }
                }, Context.BIND_AUTO_CREATE);

        }

        @Override
        protected void onDestroy()
        {
            super.onDestroy();
            super.onDetach();
            onPlayManagerListener.setValue(null);
        }

	}

    private final PlayManagerFragment playManagerFragment;

    private final FragmentActivity context;

    private PlayManager(FragmentActivity context)
    {
        this.context = context;
        String ID = "weixiaoliu";
		PlayManager.PlayManagerFragment fragment1 = (PlayManager.PlayManagerFragment) context.findFragmentByTag(ID);
		if (fragment1 == null)
		{
			playManagerFragment = new PlayManagerFragment();
			context.getFragmentPlusManager().add(0, playManagerFragment, ID).commit();  
		}
		else playManagerFragment = fragment1;
    }

	public static PlayManager getInstance(FragmentActivity activity)
	{	
        if (activity == null)return null;
      	return new PlayManager(activity);
	}

	public static PlayManager getInstance(Fragment fragment)
	{
        if (fragment == null)return null;
        return getInstance(fragment.getActivity());
    }

    @Override
    public PlayManager setOnMediaConfigurationCallback(final onMediaConfigurationCallback callback)
    {
        if (callback == null)
            return this;
        playManagerFragment.service.apply(new Observer<PlayService>(){

                @Override
                public void onChanged(PlayService p1)
                {
                    p1.setOnMediaConfigurationCallback(callback);
                }           
            });
        return this;
    }

    public PlayManager playMedia(final List<MediaMetadata> mediaList, final int index)
    {
        playManagerFragment.service.apply(new Observer<PlayService>(){
                @Override
                public void onChanged(PlayService p1)
                {
                    p1.setMediaList(mediaList);
                    p1.setMediaIndex(index);
                    p1.getSession().setMetadata(mediaList.get(index));
                    p1.getMediaController().apply(new Observer<MediaController>(){
                            @Override
                            public void onChanged(MediaController p1)
                            {
                                p1.getTransportControls().playFromMediaId(MediaMetadataFactory.analyze(mediaList.get(index)).getId(), null);                         
                            }                                 
                        });     
                }           
            });

        return this;
    }

    public PlayManager playMedia(final MediaMetadata media)
    {
        if (media == null)return this;

        playManagerFragment.service.apply(new Observer<PlayService>(){

                @Override
                public void onChanged(final PlayService p1)
                {
                    p1.setMediaList(List.of(media));
                    p1.setMediaIndex(0);
                    p1.getMediaController().apply(new Observer<MediaController>(){
                            @Override
                            public void onChanged(MediaController p2)
                            {
                                //       playManagerFragment.binder.getValue().getService().upMediaNotfication(media);
                                p1.getSession().setMetadata(media);
                                p2.getTransportControls().playFromMediaId(MediaMetadataFactory.analyze(media).getId(), null);
                            }
                        });
                }           
            });
        return this;
    }

    public PlayManager play()
    {
        playManagerFragment.service.apply(new Observer<PlayService>(){

                @Override
                public void onChanged(PlayService p1)
                {
                    p1.getMediaController().apply(new Observer<MediaController>(){

                            @Override
                            public void onChanged(MediaController p1)
                            {
                                p1.getTransportControls().play();
                            }
                        });
                }


            });
        return this;
    }

    public PlayManager pause()
    {
        playManagerFragment.service.apply(new Observer<PlayService>(){

                @Override
                public void onChanged(PlayService p1)
                {
                    p1.getMediaController().apply(new Observer<MediaController>(){

                            @Override
                            public void onChanged(MediaController p1)
                            {
                                p1.getTransportControls().pause();
                            }
                        });
                }

            });
        return this;
    }

    //@java.lang.Deprecated
    public boolean isPlaying()
    {
        if (playManagerFragment.service.getValue() != null)
            return playManagerFragment.service.getValue().isPlaying();
        return false;
    }

}
