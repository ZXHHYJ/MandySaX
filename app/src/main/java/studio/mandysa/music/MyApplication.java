package studio.mandysa.music;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import mandysax.media.DefaultPlayerManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        DefaultPlayerManager.init(this);
    }
}
