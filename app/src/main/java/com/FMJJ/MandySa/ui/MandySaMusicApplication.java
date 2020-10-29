package com.FMJJ.MandySa.ui;
import android.app.Application;
import android.content.Context;

public class MandySaMusicApplication extends Application
{
    public static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context=getApplicationContext();
    }
    
}
