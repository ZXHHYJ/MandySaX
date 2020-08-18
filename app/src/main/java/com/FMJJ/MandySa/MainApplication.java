package com.FMJJ.MandySa;
import android.app.Application;
import simon.tuke.Tuke;
import mandysax.Tools.ToastUtils;
public class MainApplication extends Application
{
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Tuke.init(this);
        ToastUtils.init(this);
	}
	
}
