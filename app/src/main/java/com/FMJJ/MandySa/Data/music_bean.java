package com.FMJJ.MandySa.Data;
import java.io.Serializable;
import java.util.List;
import mandysax.Data.get;
import org.json.JSONArray;
import org.json.JSONException;
import mandysax.Service.MusicItem;

public class music_bean extends MusicItem
{

	private String name;
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public String getUrl()
	{
		return "https://music.163.com/song/media/outer/url?id="+super.getUrl();
	}
	
	/*public String getImg() throws JSONException
	{
		if (song_img != null)
			return song_img;
		JSONArray img = new JSONArray(get.jsonstring(get.getString(url.url3 + song_id), "songs"));
		song_img = get.jsonstring(img.getJSONObject(0).getString("al"), "picUrl");
		return song_img;
	}*/
}
