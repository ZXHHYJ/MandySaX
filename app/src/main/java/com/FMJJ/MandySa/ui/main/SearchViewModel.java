package com.FMJJ.MandySa.ui.main;

import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.widget.Toast;
import com.FMJJ.MandySa.logic.SearchMusicRepository;
import java.util.ArrayList;
import java.util.List;
import mandysax.data.repository.DataCallback;
import mandysax.data.repository.Key;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.livedata.LiveData;
import mandysax.lifecycle.livedata.MutableLiveData;

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

	private void search_song(String name, final int page)
	{
		song_name = name;
		if (song_name == null)return;
        SearchMusicRepository.getAppRepository().getNetworkData(new Key().putString(SearchMusicRepository.GET_NAME_KEY, name).putInt(SearchMusicRepository.GET_PAGE_KEY, page), new DataCallback<List<MediaMetadataCompat>>(){

                @Override
                public void success(List<MediaMetadataCompat> t)
                {
                    (page == 1 ?_song_search: _song_bottom).postValue(t);
                }

                @Override
                public void failure(String errorMsg)
                {
                    (page == 1 ?_song_search: _song_bottom).postValue(null);                    
                    //Looper.prepareMainLooper();
                    Toast.makeText(getApplication(), errorMsg, 10).show();
                    //Looper.loop();
                }
            });
	}

    public String getSearchContext()
    {
        return song_name;
    }

	public void song_search(String name)
	{
		if (name == null)name = song_name;//name=null代表刷新操作
		if (name == null)return;//如果还是null就不执行任何操作
		song_page = 1;
		search_song(name, song_page);
	}

	public void song_bottom()
	{
		song_page++;
		search_song(song_name, song_page);
	}

}
