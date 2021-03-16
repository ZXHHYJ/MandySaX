package mandysax.plus.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleImpl;
import mandysax.plus.lifecycle.LifecycleOwner;

public class Fragment implements FragmentImpl,LifecycleOwner
{

	@Override
	public boolean onBackPressed()
	{
		return false;
	}

	/*
	 *mandysax实现了一个Fragment作为系统Fragment的替代品
	 *系统Fragment已废弃
	 *为在Fragment中使用ViewModel,Lifecycle,LiveData提供了便利
	 */

	private FragmentActivity mActivity;//上下文

	private String mTag;

	private final Lifecycle mLifecycle = new Lifecycle();//生命周期

	private View mView;//fragment控件

	private int mLayoutId=0;//父控件id

	private boolean mDetached;

	private boolean mRemoving;

	private boolean mResumed;

	protected LayoutInflater getLayoutInflater()
	{
		return mActivity.getLayoutInflater();
	}

	@Override
    public <T extends View> T findViewById(int id)
    {   
        if (mView != null)
            return mView.findViewById(id);
        return null;
    }

	@Override
	public LifecycleImpl getLifecycle()
	{
		return mLifecycle;
	}

    @Override
	public Context getContext()
	{
		return mActivity;
	}


	@Override
	public String getTag()
	{
		return mTag;
	}

    @Override
	public FragmentActivity getActivity()
	{
        return mActivity;
    }

    @Override
    public void startActivity(Intent intent)
	{
        mActivity.startActivity(intent);
    }

    @Override
	public void startActivity(Intent intent, Bundle options)
	{
		mActivity.startActivity(intent, options);
	}

	/*
	 *2.0.0中对isAdded()的逻辑进行了调整
	 *避免了返回true但Fragment并没有添加的bug
	 */
    @Override
	public boolean isAdded()
	{
		if (mActivity != null)
			return mActivity.getFragmentPlusManager().findFragmentByTag(mTag) != null;
		return false;
	}

    @Override
	public boolean isDetached()
	{
		return mDetached;
	}

    @Override
	public boolean isRemoving()
	{
		return mRemoving;
	}

    @Override
	public boolean isInLayout()
	{
		return mView != null;
	}

    @Override
	public boolean isResumed()
	{
		return mResumed;
	}

    @Override
	public boolean isVisible()
	{
		return mView != null ?mView.getVisibility() == View.VISIBLE: false;
	}

    @Override
	public boolean isHidden()
	{
		return !isVisible();
	}

	protected void onHiddenChanged(boolean hidden)
	{
	}

	void setTag(String tag, int id)
	{
		mTag = tag;
		mLayoutId = id;
	}

	protected void onAttach(Context context)
	{
		mActivity = (FragmentActivity) context;
		mDetached = false;
		mRemoving = false;
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		mLifecycle.onCreate();
	}

	/*
	 *2.0.0新增
	 */

	protected int onCreateView()
	{
		return 0;
	}

    protected View onCreateView(LayoutInflater inflater, ViewGroup container)
    {
        return null;
	}

	protected void onViewCreated(View view, Bundle savedInstanceState)
	{
        if (view == null)return;
		this.mView = view;	
	    getView().setVisibility(View.GONE);
		if (getViewGroup() == null)throw new NullPointerException("Can't find the parent layout of " + getClass().getCanonicalName());
		getViewGroup().addView(getView());
	}

    @Override
	public View getView()
	{
		return mView;
	}

	final ViewGroup getViewGroup()
	{
        if (mActivity == null)return null;
        return mActivity.findViewById(mLayoutId);
	}

	protected void onActivityCreated(Bundle savedInstanceState)
	{
	}

	protected void onStart()
	{
		mResumed = true;
		mLifecycle.onStart();
	}

    protected void onRestart()
    {
        mLifecycle.onRestart();
    }

	protected void onResume()
	{
		mLifecycle.onResume();
	}

	protected void onSaveInstanceState(Bundle outState)
	{
	}

	protected void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
	}

    @Deprecated
	protected void onMultiWindowModeChanged(boolean isInMultiWindowMode)
	{
	}

	protected void onConfigurationChanged(Configuration newConfig)
	{
	}

	protected void onPause()
	{	
		mLifecycle.onPause();
	}

	protected void onStop()
	{
		mResumed = false;
		mLifecycle.onStop();
	}

	protected void onDestroyView()
	{
		mRemoving = true;
		if (getView() != null)
			if (getView().getParent() != null)
			{
				((ViewGroup)getView().getParent()).removeView(getView());
			}
		/*if (!mRetainInstance)
		 {
		 mView = null;//确认不保存fragment后将其view释放
		 }*/
	}

	protected void onDestroy()
	{
		mLifecycle.onDestory();
    }

	protected void onDetach()
	{
		mDetached = true;
		mActivity = null;
	}

}
