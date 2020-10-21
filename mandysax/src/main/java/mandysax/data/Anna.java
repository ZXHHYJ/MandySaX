package mandysax.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import mandysax.core.annotation.ARRAY;
import mandysax.core.annotation.GET;
import mandysax.core.annotation.INT;
import mandysax.core.annotation.STRING;
import mandysax.utils.FieldUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Anna
{

	private Callback<Object> callback;
	private Class cls;
	private String url=getUrl();
	private String[] key=getKeyword();

	public Anna setUrl(String url)
	{
		this.url = url;
		return this;
	}

	public Anna addUrl(String url)
	{
		this.url = this.url + url;
		return this;
	}

	public Anna addKeyword(String... key)
	{
		this.key = key;
		return this;
	}

	protected String getUrl()
	{
		return "";
	}

	protected String[] getKeyword()
	{
		return null;
	}

	public <E extends Class,T> Anna enqueue(E cls, Callback<T> callback)
	{
		this.callback = (Callback<Object>) callback;
		this.cls = cls;
		return this;
	}

	public void start()
	{
		getExecutor().execute(new Runnable(){
				@Override
				public void run()
				{
					try
					{
						String get =Parsing(getString(url));
						if (get != null)
							switch (getType(cls.newInstance().getClass()))
							{
								case "ARRAY":
									JSONArray json = new JSONArray(get);
									if (json != null)
									{
										for (int i = 0;i < json.length();i++)
										{
											callback.onStart(new CallFactory().create(cls, json.getString(i)));
										}
										callback.onEnd(false);
									}
									else callback.onEnd(true);
									break;
								case "STRING":		
									callback.onStart(new CallFactory().create(cls, get));
									callback.onEnd(false);
									break;
								default:
									callback.onEnd(true);
									break;
							}
						else callback.onEnd(true);
					}
					catch (Exception e)
					{
						callback.onEnd(true);
					}
				}


			});
	}

	private String Parsing(String con)
	{
		String content=con;
		for (String string: key)
		{
			try
			{
				content = new JSONObject(content).optString(string);
			}
			catch (JSONException e)
			{
			}
		}
		return content;
	}

	private String getType(Class cls)
	{
		if (cls.isAnnotationPresent(GET.class))
			return ((GET)cls.getAnnotation(GET.class)).value();
		return null;
	}

	private static String getString(String url)
    {    
        try
        {   
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();   
			connection.setRequestMethod("GET");   
            if (connection.getResponseCode() == 200)         
                return inputStream2String(connection.getInputStream());       
        }
        catch (Exception e)
		{
			return null;
		}
		
		return null;
	}

	private static String inputStream2String(InputStream is) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1)
        {
            byteArrayOutputStream.write(i);
        }
        return byteArrayOutputStream.toString();
    }

	private class CallFactory
	{
		public Object create(Class callClass, String content)
		{
			try
			{
				Object object = callClass.newInstance();
				Class cls = object.getClass();
				if (content != null)
					for (Field field : cls.getDeclaredFields())
					{
						if (field.isAnnotationPresent(STRING.class))
						{
							STRING string = field.getAnnotation(STRING.class);
							FieldUtils.setField(field, object, new JSONObject(content).optString(string.value()));	
						}
						else
						if (field.isAnnotationPresent(INT.class))
						{
							INT Int = field.getAnnotation(INT.class);
							FieldUtils.setField(field, object, new JSONObject(content).opt(Int.value()));
						}
						else
						if (field.isAnnotationPresent(ARRAY.class))
						{
							ARRAY array = field.getAnnotation(ARRAY.class);
							FieldUtils.setField(field, object, new JSONObject(content).getJSONArray(array.value()));				
						}
					}
				return object;
			}
			catch (Exception e)
			{
				return null;
			}
		}
	}

	private static ThreadPoolExecutor executor;
    private static ThreadPoolExecutor getExecutor()
	{
        if (executor == null)
		{
            synchronized (Anna.class)
			{
                if (executor == null)
				{
                    executor = new ThreadPoolExecutor(1, 5, 60, TimeUnit.SECONDS,
													  new LinkedBlockingQueue<Runnable>());
                }
            }
        }
        return executor;
    }

}
