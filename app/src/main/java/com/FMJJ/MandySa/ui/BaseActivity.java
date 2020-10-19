package com.FMJJ.MandySa.ui;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import mandysax.lifecycle.AppCompatActivity;

public class BaseActivity extends AppCompatActivity
{
	private final void setAndroidNativeLightStatusBar()
	{
		if (VERSION.SDK_INT < 29)return;
		final View decor = getWindow().getDecorView();
		int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
		if (currentNightMode == Configuration.UI_MODE_NIGHT_NO)
		{
			decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
		}
		else
		{
			decor.setSystemUiVisibility(0);
		}
	}

    @Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		setAndroidNativeLightStatusBar();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setAndroidNativeLightStatusBar();
	}


}
