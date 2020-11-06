package mandysax.lifecycle;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends Activity implements LifecycleOwner
{

	private final Lifecycle mLifecycle = new Lifecycle();

	private List<Fragment> mBackStack;

	private ViewModelStore mViewModelStore;

	private FragmentMannger mannger;

	//private HashMap<String,Lifecycle> lifecycle;

	private NonConfigurationInstances mLastNonConfigurationInstances;

	public Fragment findFragmentByTag(Object tag)
	{
		return mannger.findFragmentByTag(tag);
	}

    public FragmentMannger getMannger()
	{
		return mannger;
	}

	protected void onAttachFragment(Fragment fragment)
	{
	}

    /*HashMap<String,Lifecycle> getLifecycles()
	 {
	 return lifecycle;
	 }*/

	ViewModelStore getViewModelStore()
	{
		return mViewModelStore;
	}

	private void resetLifecycle()
	{
		if (getApplication() != null)
		{
			NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
			if (nonConfigurationInstances != null)
			{
				mViewModelStore = nonConfigurationInstances.viewModelStore;
				mannger = nonConfigurationInstances.fragmentMannger;
				mBackStack = nonConfigurationInstances.backStack;}
			else
			{
				mViewModelStore = new ViewModelStore();
				mannger = new FragmentMannger(this);
				mBackStack = null;
			}
		}
		else throw new IllegalStateException("Big problem");//出大问题
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		if (mLastNonConfigurationInstances == null)
		{
			NonConfigurationInstances nci = new NonConfigurationInstances();
			nci.viewModelStore = mViewModelStore;
			nci.fragmentMannger = mannger;
			nci.backStack = mBackStack;
			return nci;
		}
		else return mLastNonConfigurationInstances;
	}

	@Override
	public Object getLastNonConfigurationInstance()
	{
		return super.getLastNonConfigurationInstance();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mannger.saveInstanceState(outState);
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
		mannger.multiWindowModeChanged(isInMultiWindowMode, newConfig);
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode)
	{
        super.onMultiWindowModeChanged(isInMultiWindowMode);
		mannger.multiWindowModeChanged(isInMultiWindowMode);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
        super.onConfigurationChanged(newConfig);
		mannger.configurationChanged(newConfig);
	}

	/*
	 恢复fragment
	 */
	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		mannger.resumeFragment();
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		mannger.resumeFragment();
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params)
	{
		super.setContentView(view, params);
		mannger.resumeFragment();
	}
	/*
	 恢复fragment
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		resetLifecycle();//恢复一些重要数据
		mannger.create(this, savedInstanceState);//配置变更后context已经被管理者移除了，需要重新赋值
		mLifecycle.onCreate();
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return mLifecycle;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mLifecycle.onStart();
		mannger.start();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mLifecycle.onResume();
		mannger.resume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mLifecycle.onPause();
		mannger.pause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mLifecycle.onStop();
		mannger.stop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mLifecycle.onDestory();
		mannger.destroy();
		if (!isChangingConfigurations())
		{
			mannger.clear();
			mViewModelStore.clear();		
			//lifecycle.clear();
		}
		//else lifecycle.remove(getClass().getCanonicalName());		

	}

	@Override
	public void onBackPressed()
	{
		if (mBackStack == null)
		{
			super.onBackPressed();
			return;
		}
		if (mBackStack.isEmpty())super.onBackPressed();
		else
		{	
			getMannger().remove(mBackStack.remove(mBackStack.size() - 1));
		}
	}

	static final class NonConfigurationInstances
	{
		FragmentMannger fragmentMannger;//fragment的管理者
		ViewModelStore viewModelStore;//viewmodel的管理者
	    List<Fragment> backStack;
	}

	void addToBackStack(Fragment fragment)
	{
		if (mBackStack == null)mBackStack = new ArrayList<Fragment>();
		if (fragment == null)return;
		mBackStack.add(fragment);
	}

}

