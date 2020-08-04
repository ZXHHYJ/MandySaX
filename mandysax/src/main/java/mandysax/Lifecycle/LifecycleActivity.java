package mandysax.Lifecycle;
import android.app.Activity;

public class LifecycleActivity extends Activity implements LifecycleAbstract
{
	
	private final Lifecycle lifecycle = new Lifecycle();
	
	{
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
		if(isFinishing()){
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
