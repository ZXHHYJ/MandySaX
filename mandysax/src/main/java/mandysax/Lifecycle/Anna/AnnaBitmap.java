package mandysax.Lifecycle.Anna;

import android.graphics.*;
import android.widget.*;
import java.io.*;
import java.net.*;
import java.util.*;
import mandysax.Lifecycle.Anna.AnnaEvent.*;
import mandysax.Tools.*;

public class AnnaBitmap
{
	private final List<String> list_string = new ArrayList<String>();
	private URL url;
	private ImageView view;
	private onEvent<Bitmap> onEvent;

	public AnnaBitmap addString(String keyword)
	{
		list_string.add(keyword);
		return this;
	}

	public AnnaBitmap(String url)
	{
		try
		{
			this.url = new URL(url);
		}
		catch (MalformedURLException e)
		{}
	}

	public <T> AnnaBitmap setOnEvent(onEvent<Bitmap> onEvent)
	{
		this.onEvent = onEvent;
		return this;
	}

	public AnnaBitmap setZoom(ImageView view)
	{
		this.view = view;
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
						if (list_string.size() != 0)
							url = new URL(AnnaTask.Parsing(list_string, AnnaTask.getString(url)));
						if (view != null)
							onEvent.onEnd(AnnaTask.getBitmap(url));
						else onEvent.onEnd(BitmapUtils.zoomImg(AnnaTask.getBitmap(url), view.getWidth(), view.getHeight()));
					}
					catch (IOException e)
					{
						onEvent.onError();
					}
				}});
	}
}
