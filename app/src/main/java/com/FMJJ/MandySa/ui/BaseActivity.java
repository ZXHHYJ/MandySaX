package com.FMJJ.MandySa.ui;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import mandysax.lifecycle.AppCompatActivity;

public class BaseActivity extends AppCompatActivity
{
	private final void setAndroidNativeLightStatusBar()
	{
		final View decor = getWindow().getDecorView();
		int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
		if (currentNightMode == Configuration.UI_MODE_NIGHT_YES)
		{
		    decor.setSystemUiVisibility(0);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setAndroidNativeLightStatusBar();
	}


}
