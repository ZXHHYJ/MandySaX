package mandysax.Lifecycle.Anna;

import java.io.*;
import java.net.*;
import java.util.*;
import mandysax.Lifecycle.Anna.AnnaEvent.*;
import org.json.*;

public class AnnaJsonArray
{
	private final List<String> list_string = new ArrayList<String>();
	private final String url;
	private onEvent<JSONArray> onEvent;

	public AnnaJsonArray addString(String keyword)
	{
		list_string.add(keyword);
		return this;
	}

	public AnnaJsonArray(String url)
	{
		this.url = url;
	}

	public <T> AnnaJsonArray setOnEvent(onEvent<JSONArray> onEvent)
	{
		this.onEvent = onEvent;
		return this;
	}

	public void start()
	{
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					_start();
				}});
	}

	private void _start()
	{	
		try
		{
			try
			{
				onEvent.onEnd(new JSONArray(AnnaTask.Parsing(list_string, AnnaTask.getString(new URL(url)))));
			}
			catch (IOException e)
			{
				onEvent.onError();
			}
			catch (JSONException e)
			{
				onEvent.onError();
			}
		}
		catch (Exception e)
		{
			onEvent.onError();
			e.printStackTrace();
		}
	}

}
