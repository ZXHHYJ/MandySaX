package com.FMJJ.MandySa.ViewModel;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.FMJJ.MandySa.Service.MusicService;
import mandysax.Lifecycle.LiveData.LiveData;
import mandysax.Lifecycle.LiveData.MutableLiveData;
import mandysax.Lifecycle.ViewModel.ViewModel;
import mandysax.Lifecycle.Paradrop.paradrop;

public class MainViewModel extends ViewModel
{
	public int index=0;

	private MutableLiveData<MusicService.MusicBinder> _music_binder=new MutableLiveData<MusicService.MusicBinder>();

	public LiveData<MusicService.MusicBinder> music_binder = _music_binder;

	private final MusicConnector music_connector = new MusicConnector();

    private class MusicConnector implements ServiceConnection
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
			_music_binder.setValue((MusicService.MusicBinder)paradrop.newTask("music_binder", iBinder));
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {          
        }
    }

	public void bindService(Activity activity)
    {
		activity.bindService(new Intent(activity, MusicService.class), music_connector, Context.BIND_AUTO_CREATE);
	}

}
