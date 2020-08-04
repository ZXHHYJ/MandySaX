package com.FMJJ.MandySa.Service;
import com.FMJJ.MandySa.Data.music_bean;
import com.FMJJ.MandySa.MainActivity;


public class MusicService extends mandysax.Service.MediaService<music_bean>
{

	@Override
	public Class<?> getOpenClass()
	{
		return MainActivity.class;
	}
	
}
    
