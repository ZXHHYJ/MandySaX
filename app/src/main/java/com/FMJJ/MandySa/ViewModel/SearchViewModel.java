package com.FMJJ.MandySa.ViewModel;

import com.FMJJ.MandySa.Data.url;
import java.util.ArrayList;
import java.util.List;
import mandysax.Data.get;
import mandysax.Lifecycle.LiveData.LiveData;
import mandysax.Lifecycle.LiveData.MutableLiveData;
import mandysax.Lifecycle.ViewModel.ViewModel;
import mandysax.Service.SingerItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import mandysax.Service.MusicItem;

public class SearchViewModel extends ViewModel
{

	private final MutableLiveData<List<MusicItem>> _song_search = new MutableLiveData<List<MusicItem>>();

	public final LiveData<List<MusicItem>> song_search = _song_search;

	private final MutableLiveData<List<MusicItem>> _song_bottom = new MutableLiveData<List<MusicItem>>();

	public final LiveData<List<MusicItem>> song_bottom = _song_bottom;

	public final List<MusicItem> song_list = new ArrayList<MusicItem>();

	private int song_page = 1;

	private String song_name;

	private List<MusicItem> search_song(String name, int page)
	{
		song_name = name;
		if (song_name == null)return null;
		final ArrayList<MusicItem> out = new ArrayList<MusicItem>();
		try
		{
			final String content = get.jsonstring(get.jsonstring(get.getString(url.url2 + name + "&offset=" + (page - 1) * 30), "result"), "songs");
			if (content != null)
			{
				JSONArray json = new JSONArray(content);
				if (json != null)
					for (int i = 0;i < json.length();i++)
					{
						MusicItem bean=new MusicItem();	
						JSONObject jsonObject = json.getJSONObject(i);	
						bean.setTitle(name);
						bean.setId(jsonObject.getInt("id"));
						bean.setTitle(jsonObject.getString("name"));
						JSONArray jsonArray=jsonObject.getJSONArray("artists");
						List<SingerItem> singerList = new ArrayList<SingerItem>();
						if (jsonArray != null)
							for (int a = 0;a < jsonArray.length();a++)
							{
								final SingerItem singer_congtent = new SingerItem();
								singer_congtent.setId("" + jsonArray.getJSONObject(a).getInt("id"));
								singer_congtent.setName(jsonArray.getJSONObject(a).getString("name"));
								singerList.add(singer_congtent);
							}
						bean.setSinger(singerList);
						out.add(bean);
					}
			}
		}
		catch (JSONException e)
		{}
		return out;
	}

	public void song_search(String name)
	{
		if (name == null)name = song_name;
		song_page = 1;
		_song_search.postValue(search_song(name, song_page));
	}

	public void song_bottom()
	{
		song_page++;
		_song_bottom.postValue(search_song(song_name, song_page));
	}

}
