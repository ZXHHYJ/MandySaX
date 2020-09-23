package com.FMJJ.MandySa.data;
import mandysax.data.Anna;

public class searchMusic extends Anna
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
