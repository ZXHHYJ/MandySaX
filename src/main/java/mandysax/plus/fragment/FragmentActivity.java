package mandysax.plus.fragment;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleOwner;
import mandysax.plus.lifecycle.ViewModelStore;

public class FragmentActivity extends Activity implements LifecycleOwner
{

	public static final int WXY=20030725;//记一个不可以忘记的朋友<晓柳>

	public static final String VIEWMODEL_FRAGMENT_TAG="VIEWMODEL_FRAGMENT_TAG";

	private final Lifecycle mLifecycle = new Lifecycle();

	private FragmentController mFragmentController;

	private NonConfigurationInstances mLastNonConfigurationInstances;

	private ViewModelFragment mViewModelFragment;

	public Fragment findFragmentByTag(String tag)
	{
		return getFragmentPlusManager().findFragmentByTag(tag);
	}

    public FragmentController.FragmentController2 getFragmentPlusManager()
	{
		return getFragmentController().getFragmentController2();
	}

	private FragmentController getFragmentController()
	{
		if (mFragmentController == null)
		{
			NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
			if (nonConfigurationInstances != null)
			{
				return mFragmentController = nonConfigurationInstances.fragmentController;
			}
			else return mFragmentController = new FragmentController(this);
		}
		return mFragmentController;
	}

	protected void onAttachFragment(Fragment fragment)
	{
	}

	private final ViewModelFragment getViewModelFragment()
	{
		mViewModelFragment = (FragmentActivity.ViewModelFragment) getFragmentPlusManager().findFragmentByTag(VIEWMODEL_FRAGMENT_TAG);
		if (mViewModelFragment == null)
		{
			mViewModelFragment = new ViewModelFragment();
			getFragmentPlusManager().add(0, mViewModelFragment, VIEWMODEL_FRAGMENT_TAG).commit();
			return mViewModelFragment;
		}
		return mViewModelFragment;
	}

	public final ViewModelStore getViewModelStore()
	{
		return getViewModelFragment().viewModelStore;
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		if (mLastNonConfigurationInstances == null)
		{
			NonConfigurationInstances nci = new NonConfigurationInstances();
			nci.fragmentController = mFragmentController;
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
		getFragmentController().saveInstanceState(outState);
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
		getFragmentController().multiWindowModeChanged(isInMultiWindowMode, newConfig);
	}

    @java.lang.Deprecated
	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode)
	{
        super.onMultiWindowModeChanged(isInMultiWindowMode);
		getFragmentController().multiWindowModeChanged(isInMultiWindowMode);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
        super.onConfigurationChanged(newConfig);
		getFragmentController().configurationChanged(newConfig);
	}

	/*
	 恢复fragment
	 */

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		getFragmentController().resumeFragment();
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		getFragmentController().resumeFragment();
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params)
	{
		super.setContentView(view, params);
		getFragmentController().resumeFragment();
	}

	/*
	 恢复fragment
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getFragmentController().create(this, savedInstanceState);//配置变更后context已经被管理者移除了，需要重新赋值
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
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mLifecycle.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mLifecycle.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mLifecycle.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mLifecycle.onDestory();
	}

	@Override
	public void onBackPressed()
	{
		if (getFragmentController().onBackFragment())
			return;
		else super.onBackPressed();
	}

    private static final class NonConfigurationInstances
	{
	    FragmentController fragmentController;//fragment的控制器
	}

	private static final class ViewModelFragment extends Fragment
	{
		ViewModelStore viewModelStore=new ViewModelStore();//viewmodel的管理者
	}

}

