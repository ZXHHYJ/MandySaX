package com.FMJJ.MandySa.logic.network;
import android.content.Context;
import mandysax.data.anna.Anna;

public class SearchMusicNetwork extends Anna
{

    public SearchMusicNetwork(Context context){
        super(context);
    }
    
	@Override
	public String getUrl()
	{
		return "http://47.100.93.91:3000/search?keywords=";
	}

	@Override
	public String[] getKeyword()
	{
		return new String[]{"result", "songs"};
	}
	
}
