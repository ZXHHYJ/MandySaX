package com.FMJJ.MandySa.logic;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.logic.model.SearchMusicService;
import com.FMJJ.MandySa.logic.model.contentcatalogs.MusicLibrary;
import com.FMJJ.MandySa.logic.network.SearchMusicNetwork;
import com.FMJJ.MandySa.ui.MandySaMusicApplication;
import java.util.ArrayList;
import java.util.List;
import mandysax.data.anna.NetworkCallback;
import mandysax.data.repository.DataCallback;
import mandysax.data.repository.Key;
import mandysax.data.repository.Repository;
import org.json.JSONException;

public class SearchMusicRepository extends Repository<List<MediaMetadataCompat>>
{
    public static final String GET_NAME_KEY="name";

    public static final String GET_PAGE_KEY="page";

    @Override
    public void getNetworkData(Key key, final DataCallback<List<MediaMetadataCompat>> callBack)
    {
        if (key == null)
            super.getNetworkData(key, callBack);
        String name= key.getString(GET_NAME_KEY);
        final int page= key.getInt(GET_PAGE_KEY);        
        new SearchMusicNetwork(MandySaMusicApplication.context).addUrl(name + "&offset=" + (page - 1) * 30).enqueue(SearchMusicService.class, new NetworkCallback<SearchMusicService>(){

                List<MediaMetadataCompat> list= new ArrayList<MediaMetadataCompat>();

                @Override
                public void onNetworkError()
                {         
                    callBack.failure(MandySaMusicApplication.context.getString(R.string.network_error));
                }

                @Override
                public void onLoaded(boolean loaded, SearchMusicService decodeStream)
                {
                    if (decodeStream == null)
                    {
                        Looper.prepare();
                        callBack.failure(MandySaMusicApplication.context.getString(R.string.error));
                        Looper.loop();
                        callBack.success(null);
                        return;
                    }
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
                    if (loaded)
                    {
                        callBack.success(list);
                    }
                }

            }).start();
    }

    public static SearchMusicRepository getAppRepository()
    {
        return new SearchMusicRepository();
    }

}
