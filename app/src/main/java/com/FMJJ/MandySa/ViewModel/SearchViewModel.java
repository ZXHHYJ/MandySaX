package com.FMJJ.MandySa.ViewModel;

import com.FMJJ.MandySa.Data.*;
import java.util.*;
import mandysax.Lifecycle.Anna.*;
import mandysax.Lifecycle.Anna.AnnaEvent.*;
import mandysax.Lifecycle.LiveData.*;
import mandysax.Lifecycle.ViewModel.*;
import mandysax.Service.*;
import org.json.*;

public class SearchViewModel extends ViewModel
{

	private final MutableLiveData<List<MusicItem>> _song_search = new MutableLiveData<List<MusicItem>>();

	public final LiveData<List<MusicItem>> song_search = _song_search;

	private final MutableLiveData<List<MusicItem>> _song_bottom = new MutableLiveData<List<MusicItem>>();

	public final LiveData<List<MusicItem>> song_bottom = _song_bottom;

	public final List<MusicItem> song_list = new ArrayList<MusicItem>();

	private int song_page = 1;

	private String song_name;

	private void search_song(final String name, final int page)
	{
		song_name = name;
		if (song_name == null)return;
		Anna.getString(url.url2 + name + "&offset=" + (page - 1) * 30)
			.addString("result")
			.addString("songs")
			.setOnEvent(new onEvent<String>(){

				@Override
				public void onError()
				{
				}
				
				@Override
				public void onEnd(String content)
				{
					ArrayList<MusicItem> out = new ArrayList<MusicItem>();
					List<SingerItem> singerList = new ArrayList<SingerItem>();
						try
						{
							JSONArray json = new JSONArray(content);		
								for (int i = 0;i < json.length();i++)
								{
									MusicItem bean=new MusicItem();	
									JSONObject jsonObject = json.getJSONObject(i);	
									bean.setTitle(name);
									bean.setId(jsonObject.getInt("id"));
									bean.setTitle(jsonObject.getString("name"));
									JSONArray jsonArray=jsonObject.getJSONArray("artists");		
										for (int a = 0;a < jsonArray.length();a++)
										{	
											SingerItem singer_congtent = new SingerItem();
											singer_congtent.setId( jsonArray.getJSONObject(a).getInt("id"));
											singer_congtent.setName(jsonArray.getJSONObject(a).getString("name"));
											singerList.add(singer_congtent);
										}
									bean.setSinger(singerList);
									out.add(bean);							
								}
							if (page == 1)_song_search.postValue(out);
							else _song_bottom.postValue(out);
						}
						catch (JSONException e)
						{}
				}
			}).start();
	}

	public void song_search(String name)
	{
		if (name == null)name = song_name;
		song_page = 1;
		search_song(name, song_page);
	}

	public void song_bottom()
	{
		song_page++;
		search_song(song_name, song_page);
	}

}
