package com.FMJJ.MandySa.logic.network;
import mandysax.data.Anna;

public class SearchMusicNetwork extends Anna
{

	@Override
	protected String getUrl()
	{
		return "http://47.100.93.91:3000/search?keywords=";
	}

	@Override
	protected String[] getKeyword()
	{
		return new String[]{"result", "songs"};
	}
	
}
