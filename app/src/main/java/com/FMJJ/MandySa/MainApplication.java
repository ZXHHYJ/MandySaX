package com.FMJJ.MandySa;
import android.app.Application;
import mandysax.Basic.BasicToast;
import simon.tuke.Tuke;
public class MainApplication extends Application
{
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Tuke.init(this);
        BasicToast.init(this);
	}
	
}
