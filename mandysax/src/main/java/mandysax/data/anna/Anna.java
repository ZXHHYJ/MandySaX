package mandysax.data.anna;

import android.content.Context;
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
import mandysax.core.utils.FieldUtils;
import mandysax.core.utils.NetworkUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Anna
{

	private NetworkCallback<Object> callback;
	private Class cls;
	private String url=getUrl();
	private String[] key=getKeyword();
    private Context context;
    private boolean isloaded=false;

    public static Anna with(Context context)
    {
        return new Anna(context);    
    }

    public Anna(Context context)
    {
        this.context = context;
    }

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

	public String getUrl()
	{
		return "";
	}

	public String[] getKeyword()
	{
		return null;
	}

	public <E extends Class,T> Anna enqueue(E cls, NetworkCallback<T> callback)
	{
		this.callback = (NetworkCallback<Object>) callback;
		this.cls = cls;
		return this;
	}

	public void start()
	{
        //Log.d("Anna",url);
        isloaded = false;
        if (NetworkUtil.isNetworkConnected(context))
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
                                                callback.onLoaded(isloaded = i == json.length() - 1, new CallFactory().create(cls, json.getString(i)));
                                            }
                                        }
                                        else
                                        {
                                            callback.onLoaded(isloaded = true, null);
                                        }
                                        break;
                                    case "STRING":	
                                        callback.onLoaded(isloaded = true, new CallFactory().create(cls, get));
                                        break;
                                    default:
                                        throw new NullPointerException("This loading type is not supported");
                                }                          
                            else throw new NullPointerException("The return value of this address cannot be parsed");
                        }
                        catch (Exception e)
                        {
                            callback.onLoaded(true, null);
                        }
                    }
                });
        else
        {
            callback.onNetworkError();
        }
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
