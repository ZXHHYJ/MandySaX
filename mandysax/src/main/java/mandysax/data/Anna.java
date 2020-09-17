package mandysax.data;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import mandysax.core.annotation.*;
import mandysax.utils.*;
import org.json.*;

public final class Anna
{
	private Callback<Object> callback;

	private Class cls;

	private String url;

	private String[] key;

	public Anna baseUrl(String url)
	{
		this.url = url;
		return this;
	}

	public Anna addKeyWord(String... key)
	{
		this.key = key;
		return this;
	}

	public <E extends Class,T> Anna enqueue(E cls, Callback<T> callback)
	{

		this.callback = (Callback<Object>) callback;
		this.cls = cls;
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
												callback.onSuccess(new CallFactory().create(cls, json.getString(i)));
											}
											callback.onEnd();
										}
										else callback.onFailure();
										break;
									case "STRING":		
										callback.onSuccess(new CallFactory().create(cls, get));
										callback.onEnd();
										break;
									default:
										callback.onFailure();
										break;
								}
							else callback.onFailure();
						}
						catch (Exception e)
						{
							callback.onFailure();
						}
					}
					catch (Exception e)
					{
						callback.onFailure();
					}
				}
			}).start();
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
			{}
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
			return null;       
        }
        catch (Exception e)
		{
			return null;
		}
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

}
