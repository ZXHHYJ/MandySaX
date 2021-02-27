package mandysax.plus.anna;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import mandysax.plus.anna.annotation.GET;
import mandysax.plus.anna.annotation.PATH;
import mandysax.plus.repository.Key;
import mandysax.utils.Log.LogUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Anna
{

	private NetworkCallback<Object> mCallback;

	private Class mModelClass;

	private String mUrl=getUrl();

    private volatile boolean mLoading=true;

	private int mConnectTimeout=0;

	private int mReadTimeout=0;

	@Deprecated
	public Anna addUrl(String... url)
	{
		if (getUrl() != null)
			mUrl = getUrl();
		for (String addurl:url)
			mUrl += addurl;
		return this;
	}

	public Anna setConnectTimeout(int time)
	{
		mConnectTimeout = time;
		return this;
	}

	public Anna setReadTimeout(int time)
	{
		mReadTimeout = time;
		return this;
	}

	public Anna postKey(Key postkey)
	{
		int index=0;
		mUrl = getUrl();
		for (String key:postkey.getKetSet())
		{
			mUrl += ((index == 0 ?"?": "&") + key + "=" + postkey.getObject(key));
			index++;
		}
		LogUtils.i(getClass(), mUrl);
		return this;
	}

	public abstract String getUrl()

	public abstract String[] getKeyword()

	public Anna with(String url)
	{
		mUrl = url;
		return this;
	}

	public final <E extends Class,T> Anna enqueue(E modelClass, NetworkCallback<T> callback)
	{
		this.mCallback = (NetworkCallback<Object>) callback;
		this.mModelClass = modelClass;
		return this;
	}

	public boolean isLoaded()
	{
		return mLoading;
	}

	public void start()
	{
		getExecutor().execute(new Runnable(){
				@Override
				public void run()
				{
					try
					{
						String type;
						String method;
						if (mModelClass.isAnnotationPresent(GET.class))
						{
							type = ((GET)mModelClass.getAnnotation(GET.class)).value();
							method = "GET";
						}
						else if (mModelClass.isAnnotationPresent(PATH.class))
						{
						    type = ((PATH)mModelClass.getAnnotation(PATH.class)).value();
							method = "POST";
						}
						else return;
						HttpURLConnection connection = (HttpURLConnection) new URL(mUrl).openConnection();   
						if (mConnectTimeout != 0)
							connection.setConnectTimeout(mConnectTimeout);
						if (mReadTimeout != 0)
							connection.setReadTimeout(mReadTimeout);
						connection.setRequestMethod(method);   
						connection.setInstanceFollowRedirects(true);
						if (connection.getResponseCode() == 200)         
						{
							String data= inputStream2String(connection.getInputStream());
							connection.disconnect();
							if (getKeyword() != null)
								for (String string: getKeyword())
								{
									data = new JSONObject(data).optString(string);
								}
							switch (type)
							{
								case "ARRAY":
									JSONArray json = new JSONArray(data);
									if (json != null)
									{
										for (int i = 0;i < json.length();i++)
										{
											mCallback.onLoading(mLoading = i == json.length() - 1,  CallFactory.create(mModelClass, json.getString(i)));
										}
									}
									else
									{
										mCallback.onLoading(mLoading = true, null);
									}
									break;
								default:
									mCallback.onLoading(mLoading = true,  CallFactory.create(mModelClass, data));
							}   
						}
					}
					catch (Exception e)
					{
						mCallback.onError(e.getMessage());
					}
				}
			});
	}

	private String inputStream2String(InputStream is) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1)
        {
            byteArrayOutputStream.write(i);
        }
        return byteArrayOutputStream.toString();
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
					return executor = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                }
            }
        }
        return executor;
    }

}
