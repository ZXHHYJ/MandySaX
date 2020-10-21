package com.FMJJ.MandySa.ui.main;

import android.support.v4.media.MediaMetadataCompat;
import com.FMJJ.MandySa.logic.model.SearchMusicService;
import com.FMJJ.MandySa.logic.model.contentcatalogs.MusicLibrary;
import com.FMJJ.MandySa.logic.network.SearchMusicNetwork;
import java.util.ArrayList;
import java.util.List;
import mandysax.data.Callback;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.livedata.LiveData;
import mandysax.lifecycle.livedata.MutableLiveData;
import org.json.JSONException;

public class SearchViewModel extends ViewModel
{

	//只将不可变的livedata暴露，可以更加保证数据的封装性
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
		new SearchMusicNetwork().addUrl(name + "&offset=" + (page - 1) * 30).enqueue(SearchMusicService.class, new Callback<SearchMusicService>(){

				List<MediaMetadataCompat> list= new ArrayList<MediaMetadataCompat>();

				@Override
				public void onEnd(boolean bug)
				{
					if(bug)list=null;//告诉观察者网络出错了
					(page == 1 ?_song_search: _song_bottom).postValue(list);
				}

				@Override
				public void onStart(SearchMusicService decodeStream)
				{
					String singer="";
					if (decodeStream.artists != null)
						for (int i = 0;i < decodeStream.artists.length();i++)
						{
							try
							{
								if (i != 0)
									singer += "/";
								singer += decodeStream.artists.getJSONObject(i).getString("name");
							}
							catch (JSONException e)
							{}
						}
					list.add(MusicLibrary.createMediaMetadataCompat(decodeStream.id + "", decodeStream.name, singer, ""));
				}

			}).start();
	}

	public void song_search(String name)
	{
		if (name == null)name = song_name;//name=null代表刷新操作
		if(name==null)return;//如果还是null就不执行任何操作
		song_page = 1;
		search_song(name, song_page);
	}

	public void song_bottom()
	{
		song_page++;
		search_song(song_name, song_page);
	}

}
