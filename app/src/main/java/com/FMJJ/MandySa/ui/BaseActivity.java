package com.FMJJ.MandySa.ui;
import android.content.res.*;
import android.os.*;
import android.view.*;
import mandysax.lifecycle.*;

public class BaseActivity extends AppCompatActivity
{
	private void setAndroidNativeLightStatusBar()
	{
		View decor = getWindow().getDecorView();
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
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setAndroidNativeLightStatusBar();
	}


}
