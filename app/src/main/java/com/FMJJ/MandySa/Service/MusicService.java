package com.FMJJ.MandySa.Service;
import com.FMJJ.MandySa.*;
import com.FMJJ.MandySa.Data.*;
import mandysax.Service.*;


public class MusicService extends MediaService
{

	@Override
	public int getSinger()
	{
		return R.drawable.singer;
	}

	@Override
	public int getSmallIcon()
	{
		return R.mipmap.ic_music;
	}

	@Override
	public int getLastSong()
	{
		return R.mipmap.ic_skip_previous_outline;
	}

	@Override
	public int getPlayOfPause()
	{
		return R.mipmap.ic_play;
	}

	@Override
	public int getNextSong()
	{
		return R.mipmap.ic_skip_next_outline;
	}
	

	@Override
	public String getUrl()
	{
		return "https://music.163.com/song/media/outer/url?id=";
	}

	@Override
	public Class getActvity()
	{
		return MainActivity.class;
	}
	
}
    
