package simon.tsuki;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import simon.tuke.Tuke;

public class CommonEngine implements Tsuki.Engine {
    private boolean disk,memory;
    public CommonEngine(boolean isdiskcache, boolean ismemorycache) {
        this.disk = isdiskcache;
        this.memory = ismemorycache;
    }
    @Override
    public Bitmap getBitmap(Tsuki.TsukiTask task) {
        Bitmap bmp = null;
        if (memory)
			bmp = MemoryCache.Common.getBitmapFromMemCache(task.getUrl());
        if (bmp != null)
			return bmp;
        if (disk)
			bmp = Tuke.getBitmap(task.getUrl(), null);
        if (bmp != null)
			return bmp;
        return webget(task);
    }
    private Bitmap webget(Tsuki.TsukiTask task) {
		Bitmap bmp;
        try {
            URL myurl = new URL(task.getUrl());
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(2000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);//读取图像数据
            if (disk)
                Tuke.write(task.getUrl(), bmp);
            if (memory)
                MemoryCache.Common.addBitmapToMemoryCache(task.getUrl(), bmp);
            is.close();
            return bmp;
        } catch (Exception e) {
            return task.getErrorBitmap();
        }
    }
    enum MemoryCache {
        Common();
        private static LruCache<String, Bitmap> mMemoryCache;
        public  static void setMaxCache(int max){
            refresh(max);
        }
        private static void refresh(int max){
            mMemoryCache = new LruCache<String, Bitmap>(100000) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        MemoryCache(){
            refresh((int)(Runtime.getRuntime().maxMemory() / 1024));
        }
        //添加Bitmap到内存缓存中去
        public void addBitmapToMemoryCache(String url, Bitmap bitmap) {
            if (getBitmapFromMemCache(url) == null) 
                mMemoryCache.put(url, bitmap);
        }
        //内存缓存中获取对应key的Bitmap
        public Bitmap getBitmapFromMemCache(String url) {
            return mMemoryCache.get(url);
        }
        //从缓存中删除指定的Bitmap
        public void removeBitmapFromMemory(String url) {
            mMemoryCache.remove(url);
        }
    }
}
