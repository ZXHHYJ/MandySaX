package com.FMJJ.MandySa.model;

import android.support.v4.media.*;
import com.FMJJ.MandySa.data.*;
import com.FMJJ.MandySa.service.contentcatalogs.*;
import java.util.*;
import mandysax.data.*;
import mandysax.lifecycle.*;
import org.json.*;

public class SearchViewModel extends ViewModel
{

	private final MutableLiveData<List<MediaMetadataCompat>> _song_search = new MutableLiveData<List<MediaMetadataCompat>>();

	public final LiveData<List<MediaMetadataCompat>> song_search = _song_search;

	private final MutableLiveData<List<MediaMetadataCompat>> _song_bottom = new MutableLiveData<List<MediaMetadataCompat>>();

	public final LiveData<List<MediaMetadataCompat>> song_bottom = _song_bottom;

	public final List<MediaMetadataCompat> song_list = new ArrayList<MediaMetadataCompat>();

	private int song_page = 1;

	private String song_name;

	private void search_song(final String name, final int page)
	{
		song_name = name;
		if (song_name == null)return;
		new Anna().baseUrl(url.url2 + name + "&offset=" + (page - 1) * 30).addKeyWord("result", "songs").enqueue(musicGet.class, new Callback<musicGet>(){

				List<MediaMetadataCompat> list= new ArrayList<MediaMetadataCompat>();

				@Override
				public void onEnd()
				{
					if (page == 1)
					{
						_song_search.postValue(list);
					}
					else _song_bottom.postValue(list);
				}

				@Override
				public void onSuccess(musicGet decodeStream)
				{
					String singer="";
					if (decodeStream.artists != null)
						for (int i = 0;i < decodeStream.artists.length();i++)
						{
							try
							{
								if(i!=0)
									singer+="/";
								singer +=decodeStream.artists.getJSONObject(i).getString("name");
							}
							catch (JSONException e)
							{}
						}
					list.add(MusicLibrary.createMediaMetadataCompat(decodeStream.id + "", decodeStream.name, singer, ""));
				}

				@Override
				public void onFailure()
				{
					System.out.println("出错");
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
