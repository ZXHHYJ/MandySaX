package studio.mandysa.music;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.soloader.SoLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import mandysax.media.DefaultPlayerManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, false);
        Fresco.initialize(this);
        DefaultPlayerManager.init(this);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                /*.showStubImage(R.drawable.ic_stub) // 设置图片下载期间显示的图片
                .showImageOnLoading(R.drawable.ic_empty)    //设置下载过程中图片显示
                .showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片*/
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
