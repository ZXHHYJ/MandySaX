package mandysax.lifecycle;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment implements LifecycleOwner
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
	
	//tag
	private Object tag;

	public final <T extends View> T findViewById(int id)
	{
        if (isInLayout())
			return view.findViewById(id);
        throw new NullPointerException("layout not loaded");
	}

	protected LayoutInflater getLayoutInflater()
	{
		return mActivity.getLayoutInflater();
	}
	
	void setTag(Object tag){
		if(this.tag==null)
		this.tag=tag;
	}
	
	public Object getTag(){
		return tag;
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return mLifecycle;
	}

	public Intent getIntent()
	{
		return mIntent == null ?mIntent = new Intent(): mIntent;
	}

	public Context getContext()
	{
		return mActivity;
	}

	public FragmentActivity getActivity()
	{
        return mActivity;
    }

    public void startActivity(Intent intent)
	{
        mActivity.startActivity(intent);
    }

	public void startActivity(Intent intent, Bundle options)
	{
		mActivity.startActivity(intent, options);
	}

	public boolean isAdded()
	{
		return mAdded;
	}

	public boolean isDetached()
	{
		return mDetached;
	}

	public boolean isRemoving()
	{
		return mRemoving;
	}

	public boolean isInLayout()
	{
		return view != null;
	}

	public boolean isResumed()
	{
		return mResumed;
	}

	public boolean isVisible()
	{
		return view != null ?view.getVisibility() == View.VISIBLE: false;
	}

	public boolean isHidden()
	{
		return !isVisible();
	}

	protected void onHiddenChanged(boolean hidden)
	{
	}

	public void setRetainInstance(boolean retain)
	{
		this.mRetainInstance = retain;
	}


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
		view.setVisibility(View.GONE);
		if (getViewGroup() != null)
		{
			getViewGroup().addView(getView());
			return;
		}
		throw new NullPointerException("ViewGroup is null");
	}

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
