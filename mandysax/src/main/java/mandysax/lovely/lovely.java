package mandysax.lovely;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import mandysax.Lifecycle.LifecycleAbstract;

public class lovely
{

	public static lovelyTask with(LifecycleAbstract p0)
	{
		return new lovelyTask(p0);
	}

	protected static Bitmap get(lovelyTask p0)
	{
		try
		{
            URL myurl = new URL(p0.getUrl());
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            //conn.setConnectTimeout(5000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
			Bitmap bmp = BitmapFactory.decodeStream(is);//读取图像数据
            is.close();
            return bmp;
        }
		catch (Exception e)
		{
            return null;
        }
	}


}
 
