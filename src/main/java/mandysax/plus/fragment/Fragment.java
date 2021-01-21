package mandysax.plus.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleOwner;

public class Fragment implements FragmentImpl,LifecycleOwner
{

	//context
	private FragmentActivity mActivity;

	//lifecycle
	private final Lifecycle mLifecycle = new Lifecycle();

	//fragment layout
	private View view;

	//main layout
	private int layoutRes=0;

	//fragment is addsd
	private boolean mAdded;

	//fragment is Detached
	private boolean mDetached;

	//fragment is removing
	private boolean mRemoving;

	//fragment is running
	private boolean mResumed;

	//RetainInstance is false, which means that the instance will not be saved after the configuration is changed, and the default is true
	private boolean mRetainInstance=true;

	//store each fragment callback data.
	private Intent mIntent;

	protected LayoutInflater getLayoutInflater()
	{
		return mActivity.getLayoutInflater();
	}

    public <T extends View> T findViewById(int id)
    {   
        if (view != null)
            return view.findViewById(id);
        return null;
    }

	@Override
	public Lifecycle getLifecycle()
	{
		return mLifecycle;
	}

    @Override
	public Intent getIntent()
	{
		return mIntent == null ?mIntent = new Intent(): mIntent;
	}

    @Override
	public Context getContext()
	{
		return mActivity;
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

    @Override
	public boolean isAdded()
	{
		return mAdded;
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
		return view != null;
	}

    @Override
	public boolean isResumed()
	{
		return mResumed;
	}

    @Override
	public boolean isVisible()
	{
		return view != null ?view.getVisibility() == View.VISIBLE: false;
	}

    @Override
	public boolean isHidden()
	{
		return !isVisible();
	}

	protected void onHiddenChanged(boolean hidden)
	{
	}

    @Override
	public void setRetainInstance(boolean retain)
	{
		this.mRetainInstance = retain;
	}


    @Override
	public boolean getRetainInstance()
	{
		return mRetainInstance;
	}

	protected void onAttach(Context context)
	{
		if (mActivity == null)//Repeated binding is not allowed
			mActivity = (FragmentActivity) context;
		mDetached = false;
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		//getActivity().getLifecycles().put(getClass().getCanonicalName(), mLifecycle);
		mLifecycle.onCreate();
        mAdded = true;
		mRemoving = false;
	}

    protected View onCreateView(LayoutInflater inflater, ViewGroup container)
    {
        return null;
	}

	protected View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return null;
	}

	protected void onViewCreated(View view, Bundle savedInstanceState)
	{
		if (view == null)return;
		this.view = view;
		if (view.getVisibility() != View.GONE)
			view.setVisibility(View.GONE);
		if (getViewGroup() != null)
		{
			getViewGroup().addView(getView());
		}
	}

    @Override
	public View getView()
	{
		return view;
	}

	protected final void setViewGroupId(int id)
	{
		if (layoutRes == 0)layoutRes = id;
	}

	protected ViewGroup getViewGroup()
	{
		if (mActivity != null)
			return mActivity.findViewById(layoutRes);
		return null;
	}

	protected void onActivityCreated(Bundle savedInstanceState)
	{
	}

	protected void onStart()
	{
		mResumed = true;
		mLifecycle.onStart();
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
		//mInLayout = false;
		mRemoving = true;
		if (getViewGroup() != null && getView() != null)
			getViewGroup().removeView(getView());//获取根布局，移出fragment布局
		if (!mRetainInstance)
		{
			mAdded = false;
			view = null;//确认不保存fragment后将其view释放
		}
		//root = null;//取消对根部局的引用，避免内存泄露
	}

	protected void onDestroy()
	{
		mLifecycle.onDestory();
		//getActivity().getLifecycles().remove(getClass().getCanonicalName());
	}

	protected void onDetach()
	{
		mDetached = true;
		mActivity = null;
		//mPage = null;
	}

}
