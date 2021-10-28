package studio.mandysa.music;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import mandysax.media.DefaultPlayerManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);//初始化图片加载库
        DefaultPlayerManager.init(this);//初始化播放管理器
    }
}
