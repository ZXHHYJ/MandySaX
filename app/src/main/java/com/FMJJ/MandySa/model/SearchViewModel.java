package com.FMJJ.MandySa.model;

import android.support.v4.media.MediaMetadataCompat;
import com.FMJJ.MandySa.data.musicGet;
import com.FMJJ.MandySa.data.searchMusic;
import com.FMJJ.MandySa.service.contentcatalogs.MusicLibrary;
import java.util.ArrayList;
import java.util.List;
import mandysax.data.Callback;
import mandysax.lifecycle.LiveData;
import mandysax.lifecycle.MutableLiveData;
import mandysax.lifecycle.ViewModel;
import org.json.JSONException;
import android.widget.Toast;

public class SearchViewModel extends ViewModel
{
	
	public static int BUG=2;
	
	public static int LOAD=0;

	private final MutableLiveData<List<MediaMetadataCompat>> _song_search = new MutableLiveData<List<MediaMetadataCompat>>();

	public final LiveData<List<MediaMetadataCompat>> song_search = _song_search;

	private final MutableLiveData<List<MediaMetadataCompat>> _song_bottom = new MutableLiveData<List<MediaMetadataCompat>>();

	public final LiveData<List<MediaMetadataCompat>> song_bottom = _song_bottom;

	private final MutableLiveData<Integer> _status = new MutableLiveData<Integer>();
	
	public final LiveData<Integer>  status=_status;
	
	public final List<MediaMetadataCompat> song_list = new ArrayList<MediaMetadataCompat>();

	private int song_page = 1;

	private String song_name="";
	
	private void search_song(final String name, final int page)
	{
		song_name = name;
		if (song_name == null)return;
		new searchMusic().addUrl(name + "&offset=" + (page - 1) * 30).enqueue(musicGet.class, new Callback<musicGet>(){

				List<MediaMetadataCompat> list= new ArrayList<MediaMetadataCompat>();

				@Override
				public void onEnd(boolean bug)
				{
					if(!bug){
					if (page == 1)
					{
						_song_search.postValue(list);
					}
					else _song_bottom.postValue(list);
					}
					else _status.postValue(BUG);
				}

				@Override
				public void onStart(musicGet decodeStream)
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

			}).start();
	}
	
	public String getSearchName(){
		return song_name;
	}

	public void song_search(String name)
	{
		_status.setValue(LOAD);
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
