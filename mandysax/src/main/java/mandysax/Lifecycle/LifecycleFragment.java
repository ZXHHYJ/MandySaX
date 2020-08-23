package mandysax.Lifecycle;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import mandysax.Lifecycle.LifecycleEvent.*;

public abstract class LifecycleFragment extends Fragment implements LifecycleAbstract
{

	private final Lifecycle lifecycle = new Lifecycle();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		LifecycleManagement.LifecycleData.add(lifecycle);
	}
	
	public abstract int getLayoutId();

    public abstract void initView(View view);

	@Override
	public Lifecycle getLifecycle()
	{
		return lifecycle;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    final View view = inflater.inflate(getLayoutId(), null);
		initView(view);
		return view;
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (context instanceof LifecycleActivity)
		{((LifecycleActivity) context).getLifecycle().KillEvent(new LifecycleViewModel(){
					@Override
					public void onKill()
					{
						LifecycleFragment.this.lifecycle.onKill();
					}		
				});
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		lifecycle.onStart();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		lifecycle.onStop();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		lifecycle.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		lifecycle.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		lifecycle.onDestory();
		LifecycleManagement.LifecycleData.remove(lifecycle);
	}

}
