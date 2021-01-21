package mandysax.plus.fragment;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Gallery.LayoutParams;
import java.util.ArrayList;
import java.util.List;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleOwner;
import mandysax.plus.lifecycle.ViewModelStore;

public class FragmentActivity extends Activity implements LifecycleOwner
{

	public final int WXY=20030725;//记一个不可以忘记的朋友<晓柳>

	private final Lifecycle mLifecycle = new Lifecycle();

	private List<Fragment> mBackStack;

	private ViewModelStore mViewModelStore;

	private FragmentMannger mannger;

	private String mainFragmentTAG;

	private NonConfigurationInstances mLastNonConfigurationInstances;

	public Fragment findFragmentByTag(Object tag)
	{
		return getMannger().findFragmentByTag(tag);
	}

	public Fragment getActivityFragment()
	{
		Fragment fragment;
		if ((fragment = getMannger().findFragmentByTag(mainFragmentTAG)) == null)throw new RuntimeException("this activity don't use Fragment is Layout");	
		return fragment;
	}

    public FragmentMannger getMannger()
	{
		if (mannger == null)
		{
			NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
			if (nonConfigurationInstances != null)
			{
				return mannger = nonConfigurationInstances.fragmentMannger;
			}
			else return mannger = new FragmentMannger(this);
		}
		return mannger;
	}

	protected void onAttachFragment(Fragment fragment)
	{
	}

	public final ViewModelStore getViewModelStore()
	{
		if (mViewModelStore == null)
		{
			NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
			if (nonConfigurationInstances != null)
			{
				return mViewModelStore = nonConfigurationInstances.viewModelStore;
			}
			else return mViewModelStore = new ViewModelStore();
		}
		return mViewModelStore;
	}

	private void resetBackStack()
	{
		if (getApplication() != null)
		{
			NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
			if (nonConfigurationInstances != null)
			{
				mBackStack = nonConfigurationInstances.backStack;}
			else
			{
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
		getMannger().saveInstanceState(outState);
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
		getMannger().multiWindowModeChanged(isInMultiWindowMode, newConfig);
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode)
	{
        super.onMultiWindowModeChanged(isInMultiWindowMode);
		getMannger().multiWindowModeChanged(isInMultiWindowMode);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
        super.onConfigurationChanged(newConfig);
		getMannger().configurationChanged(newConfig);
	}

	/*
	 恢复fragment
	 */
	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		getMannger().resumeFragment();
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		getMannger().resumeFragment();
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params)
	{
		super.setContentView(view, params);
		getMannger().resumeFragment();
	}

	public void setContentView(Class fragmentClass)
	{
		FrameLayout layout=new FrameLayout(this);
		layout.setId(WXY);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(layout);
		mainFragmentTAG = fragmentClass.getCanonicalName() + WXY;
		Fragment fragment;
		if ((fragment = getMannger().findFragmentByTag(mainFragmentTAG)) == null)
			getMannger().replace(WXY, fragmentClass, mainFragmentTAG);
		else getMannger().show(fragment);
	}

	/*
	 恢复fragment
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		resetBackStack();
		getMannger().create(this, savedInstanceState);//配置变更后context已经被管理者移除了，需要重新赋值
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
		getMannger().start();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mLifecycle.onResume();
		getMannger().resume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mLifecycle.onPause();
		getMannger().pause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mLifecycle.onStop();
		getMannger().stop();	
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mannger != null)
			mannger.destroy();
		mLifecycle.onDestory();
		if (!isChangingConfigurations())
		{
			if (mannger != null)
				mannger.clear();
			if (mViewModelStore != null)
				mViewModelStore.clear();		
		}
	}

	@Override
	public void onBackPressed()
	{
		if (mBackStack == null)
		{
			super.onBackPressed();
			return;
		}
		else
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

	public void addToBackStack(Fragment fragment)
	{
		if (mBackStack == null)mBackStack = new ArrayList<Fragment>();
		if (fragment == null)return;
		mBackStack.add(fragment);
	}

}

