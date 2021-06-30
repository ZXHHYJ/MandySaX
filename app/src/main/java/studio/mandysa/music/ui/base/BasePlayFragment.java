package studio.mandysa.music.ui.base;

import android.media.session.PlaybackState;
import android.os.Bundle;

import studio.mandysa.music.service.PlayManager;

public abstract class BasePlayFragment extends BaseFragment
{
    public boolean getIsPlaying()
    {
        return PlayManager.getInstance(this).isPlaying();
    }

    public abstract void setPlayModel();

    public abstract void setPauseModel();
    
    @Override
    protected void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        PlayManager.getInstance(this).getPlaybackStateLiveData().observe(this, p1 -> {
            if (p1 == null)return;
            if (p1.getState() == PlaybackState.STATE_PLAYING)
            {
                setPauseModel();
                return;
            }
            setPlayModel();
        });
    }



}
