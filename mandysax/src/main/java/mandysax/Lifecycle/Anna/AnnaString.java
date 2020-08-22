package mandysax.Lifecycle.Anna;

import java.io.*;
import java.net.*;
import java.util.*;
import mandysax.Lifecycle.Anna.AnnaEvent.*;

public class AnnaString
{

	private final List<String> list_string = new ArrayList<String>();
	private final String url;
	private onEvent<String> onEvent;

	public AnnaString addString(String keyword)
	{
		list_string.add(keyword);
		return this;
	}

	public AnnaString(String url)
	{
		this.url=url;
	}

	public <T> AnnaString setOnEvent(onEvent<String> onEvent)
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
					try
					{
						onEvent.onEnd(AnnaTask.Parsing(list_string, AnnaTask.getString(new URL(url))));
					}
					catch (IOException e)
					{
						onEvent.onError();
					}			
					}
			}).start();
		
	}
}
