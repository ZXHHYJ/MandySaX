package mandysax.plus.media;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import mandysax.plus.lifecycle.livedata.Observer;
import mandysax.plus.media.callback.onMediaConfigurationCallback;

public final class MediaSessionCompatCallback extends MediaSession.Callback
{

    @Override
    public boolean onMediaButtonEvent(Intent mediaButtonIntent)
    {
        switch (mediaButtonIntent.getExtras().getInt(Media.PLAY_BACK_STATE))
        {
            case PlaybackState.STATE_PLAYING:
                onPlay();
                break;
            case PlaybackState.STATE_PAUSED:
                onPause();
                break;
            case PlaybackState.STATE_SKIPPING_TO_NEXT:
                onSkipToNext();
                break;
            case PlaybackState.STATE_SKIPPING_TO_PREVIOUS:
                onSkipToPrevious();
                break;
            case PlaybackState.STATE_STOPPED:
                /*  if (isPlaying)
                 pause();*/
                onStop();
                break;
        }
        return super.onMediaButtonEvent(mediaButtonIntent);
    }
    
    private final PlayService service;

    private final MediaSession session;      

    private MediaPlayer mediaPlayer;

    private AudioManager mAm;

    public MediaSessionCompatCallback(PlayService service)
    {
        this.service = service;
        session = service.getSession();
        mAm = (AudioManager)service.getSystemService(Context.AUDIO_SERVICE);
    }

    private void setNewState(int state)
    {
        if (session != null)
            session.setPlaybackState(new PlaybackState.Builder().setState(state, 999, 1).build());
        //  isPlaying.setValue(state == PlaybackState.STATE_PLAYING);
    }

    @Override
    public void onSeekTo(long pos)
    {
        super.onSeekTo(pos);
        if (mediaPlayer != null)  
            mediaPlayer.seekTo(pos, MediaPlayer.SEEK_CLOSEST);
    }

    @Override
    public void onPlay()
    {
        super.onPlay();
        if (mediaPlayer != null)
        {
            mediaPlayer.start();
            setNewState(PlaybackState.STATE_PLAYING);
        }
    }

    @Override
    public void onPrepare()
    {
        super.onPrepare();
        if (mediaPlayer != null)
        {
            mediaPlayer.prepareAsync();
            setNewState(PlaybackState.STATE_CONNECTING);
            requestFocus();
        }
    }

    private  AudioManager.
    OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange)
        {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {
                onPause();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                onPlay();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                mAm.abandonAudioFocus(afChangeListener);
                onPause();
            }
        }
    };

    private boolean requestFocus()
    {
        int result = mAm.requestAudioFocus(afChangeListener,
                                           // Use the music stream.
                                           AudioManager.STREAM_MUSIC,
                                           // Request permanent focus.
                                           AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    @Override
    public void onPlayFromMediaId(final String mediaId, Bundle extras)
    {
        super.onPlayFromMediaId(mediaId, extras);
        if (!requestFocus())
            return;
        if (mediaPlayer != null)
        {   
            mediaPlayer.reset();
        }
        {
            if (mediaPlayer == null)
            {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);    
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

                        @Override
                        public void onPrepared(MediaPlayer p1)
                        {
                            onPlay();
                        }

                    });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mediaPlayer, int i, int i1)
                        {
                            setNewState(PlaybackState.STATE_ERROR);
                            return false;
                        }
                    });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer)
                        {
                            onSkipToNext();
                        }
                    });
            }
            service.getOnMediaConfigurationCallback().apply(new Observer<onMediaConfigurationCallback>(){

                    @Override
                    public void onChanged(onMediaConfigurationCallback p1)
                    {
                        try
                        {
                            mediaPlayer.setDataSource(p1.getUrl(mediaId));
                            onPrepare();
                        }
                        catch (Exception e)
                        {
                            setNewState(PlaybackState.STATE_ERROR);
                        }                    
                    }
                });            

        }
    }

    @Override
    public void onSkipToNext()
    {
        super.onSkipToNext();
        service.setMediaIndex(service.getMediaIndex() + 1);
    }

    @Override
    public void onSkipToPrevious()
    {
        super.onSkipToPrevious();
        service.setMediaIndex(service.getMediaIndex() - 1);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mediaPlayer != null)
        {               
            setNewState(PlaybackState.STATE_PAUSED);  
            mediaPlayer.pause();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();        
        setNewState(PlaybackState.STATE_STOPPED);
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null; 
        session.release();
    }

}
