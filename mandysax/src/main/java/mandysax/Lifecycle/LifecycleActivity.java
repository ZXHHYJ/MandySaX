package mandysax.Lifecycle;
import android.app.*;
import android.content.res.*;
import android.os.*;
import android.view.*;

public class LifecycleActivity extends Activity implements LifecycleAbstract
{

	private final Lifecycle lifecycle = new Lifecycle();

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
			decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
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
		setAndroidNativeLightStatusBar();
		super.onCreate(savedInstanceState);
		LifecycleManagement.LifecycleData.add(lifecycle);
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return lifecycle;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		lifecycle.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		lifecycle.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		lifecycle.onPause();
		if (isFinishing())
		{
			lifecycle.onKill();
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		lifecycle.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		lifecycle.onDestory();
		if (!isChangingConfigurations())
		{
			lifecycle.onKill();
		}
		LifecycleManagement.LifecycleData.remove(lifecycle);
	}

}
