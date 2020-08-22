//
// Decompiled by Jadx - 1212ms
//
package mandysax.Lifecycle.Anna;

import java.io.*;
import java.net.*;
import java.util.*;
import mandysax.Lifecycle.Anna.AnnaEvent.*;
import org.json.*;

public class AnnaJsonArray
{
    private final List<String> list_string = new ArrayList<String>();
    private onEvent<JSONArray> onEvent;
    private URL url;

    public AnnaJsonArray addString(String str)
	{
        this.list_string.add(str);
        return this;
    }

    public AnnaJsonArray(String str)
	{
        try
		{
            this.url = new URL(str);
        }
		catch (MalformedURLException e)
		{
        }
    }

    public <T> AnnaJsonArray setOnEvent(onEvent<JSONArray> onevent)
	{
        this.onEvent = onevent;
        return this;
    }

    public void start()
	{
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					_start();
				}
			}).start();
    }

    private void _start()
	{
        try
		{
            this.onEvent.onEnd(new JSONArray(AnnaTask.Parsing(this.list_string, AnnaTask.getString(this.url))));
        }
		catch (IOException e)
		{
            this.onEvent.onError();
        }
		catch (JSONException e2)
		{
        }
    }
}

