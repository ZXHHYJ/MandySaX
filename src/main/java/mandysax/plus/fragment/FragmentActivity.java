package mandysax.plus.fragment;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleImpl;
import mandysax.plus.lifecycle.LifecycleOwner;
import mandysax.plus.lifecycle.ViewModelStore;
import mandysax.plus.lifecycle.ViewModelStoreImpl;

public class FragmentActivity extends Activity implements FragmentActivityImpl,LifecycleOwner
{

	//记一个不可以忘记的朋友<晓柳>

	private final Lifecycle mLifecycle = new Lifecycle();

	private FragmentController mFragmentController;

	private ViewModelStore mViewModelStore;

	private NonConfigurationInstances mLastNonConfigurationInstances;

	private static final LayoutInflaterFactoryV21 sLayoutInflaterFactoryV21=new LayoutInflaterFactoryV21();

    @Override
    public FragmentController2Impl getFragmentPlusManager()
	{
		return getFragmentController().getFragmentController2();
	}

	private FragmentControllerImpl getFragmentController()
	{
		if (mFragmentController == null)
		{
            getLastNonConfigurationInstance();
            if (mFragmentController == null)mFragmentController = new FragmentController(this);
            return mFragmentController;
		}
		return mFragmentController;
	}

    @Override
	public ViewModelStoreImpl getViewModelStore()
	{
		if (mViewModelStore == null)
		{
            getLastNonConfigurationInstance();
            if (mViewModelStore == null)mViewModelStore = new ViewModelStore();
            return mViewModelStore;
		}
		return mViewModelStore;
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		if (mLastNonConfigurationInstances == null)
		{
			NonConfigurationInstances nci = new NonConfigurationInstances();
			nci.fragmentController = mFragmentController;
			nci.viewModelStore = mViewModelStore;
			return nci;
		}
		else return mLastNonConfigurationInstances;
	}

	@Override
	public Object getLastNonConfigurationInstance()
	{
		NonConfigurationInstances nci=(FragmentActivity.NonConfigurationInstances) super.getLastNonConfigurationInstance();
		if (nci == null)return null;
		if (mFragmentController == null)mFragmentController = nci.fragmentController;
		if (mViewModelStore == null)mViewModelStore = nci.viewModelStore;
		return nci;
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

    @Deprecated
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
		getLayoutInflater().setFactory2(sLayoutInflaterFactoryV21);
		super.onCreate(savedInstanceState);
		getFragmentController().create(this, savedInstanceState);//配置变更后context已经被管理者移除了，需要重新赋值
		mLifecycle.onCreate();
	}
    
	@Override
	public LifecycleImpl getLifecycle()
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
    protected void onRestart()
    {
        super.onRestart();
        mLifecycle.onRestart();
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
		if (getFragmentController().getFragmentController2().popBackStack())
			return;
		else super.onBackPressed();
	}

    private static final class NonConfigurationInstances
	{
	    FragmentController fragmentController;//fragment的控制器
		ViewModelStore viewModelStore=new ViewModelStore();//viewmodel的管理者
	}

}

