package mandysax.lifecycle;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mandysax.design.FragmentPage;
import mandysax.lifecycle.Lifecycle.Event;

public class Fragment implements FragmentImpl,LifecycleOwner
{

	private FragmentActivity mActivity;//上下文

	private final Lifecycle mLifecycle = new Lifecycle();//生命周期

	private View view;//fragment布局

	private int layoutRes=0;//主布局

	//private ViewGroup root;

	private FragmentPage mPage;//fragment page

	private boolean mAdded;//fragment已添加？

	private boolean mDetached;//fragment已解绑activity？

	private boolean mRemoving;//fragment正在移除？

	private boolean mInLayout;//fragment布局已经加载?

	private boolean mResumed;//fragment处于可见状态？

	private boolean mVisible;//fragment正在显示？

	private boolean mRetainInstance=true;//fragment为假表示在配置变更后不保存其实例，默认为真

	private Intent mIntent;

	public final <T extends android.view.View> T findViewById(int id)
	{
		return view.findViewById(id);
	}

	@Override
	public LayoutInflater getLayoutInflater()
	{
		return mActivity.getLayoutInflater();
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return mLifecycle;
	}

	@Override
	public void setFragmentPage(FragmentPage page)
	{
		if (this.mPage == null)
			this.mPage = page;
	}

	@Override
	public void startFragment(Class fragment)
	{
		if (mPage != null)
		{
			mPage.startFragment(this, fragment);
			return;
		}
		try
		{
			final Fragment startfragment=(Fragment) fragment.newInstance();
			if(startfragment==null)return;
			mActivity.getMannger().add(getViewGroup().getId(), startfragment).show(startfragment).addToBackStack();
			startfragment.getLifecycle().addObsever(new LifecycleObserver(){

					@Override
					public void Observer(Lifecycle.Event State)
					{
						if (State == Lifecycle.Event.ON_DESTORY)
						{
							onFragmentResult(startfragment.getIntent());
						}
					}
				});
		}
		catch (InstantiationException e)
		{
			throw new InstantiationError("Cannot instantiate "+fragment.getName());
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalAccessError("Illegal access to "+fragment.getName());
		}
	}

	public Intent getIntent()
	{
		return mIntent == null ?mIntent = new Intent(): mIntent;
	}

	public void onFragmentResult(Intent data)
	{
		//此处是一个空方法
	}

	@Override
	public Context getContext()
	{
		return mActivity;
	}

	public FragmentActivity getActivity()
	{
        return mActivity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container)
	{
		return null;
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
		return mInLayout;
	}

	@Override
	public boolean isResumed()
	{
		return mResumed;
	}

	@Override
	public boolean isVisible()
	{
		return mVisible;
	}

	@Override
	public boolean isHidden()
	{
		return !mVisible;
	}

	@Override
	public void onHiddenChanged(boolean hidden)
	{
	}

	@Override
	public void setRetainInstance(boolean retain)
	{
		mRetainInstance = retain;
	}

	@Override
	public boolean getRetainInstance()
	{
		return mRetainInstance;
	}

	@Override
	public void onAttach(Context context)
	{
		if (mActivity == null)//不得重复绑定
			mActivity = (FragmentActivity) context;
		mDetached = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//getActivity().getLifecycles().put(getClass().getCanonicalName(), mLifecycle);
		mLifecycle.onCreate();
		mRemoving = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return null;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		if (view == null)return;
		this.view = view;
		mInLayout = true;
		mAdded = true;
		if (getViewGroup() != null)
			getViewGroup().addView(getView());
		//这里记得放报错信息
	}

	@Override
	public View getView()
	{
		return view;
	}

	void setViewGroupId(int id)
	{
		if (layoutRes == 0)layoutRes = id;
	}

	ViewGroup getViewGroup()
	{
		if (mActivity != null)
			return mActivity.findViewById(layoutRes);
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
	}

	@Override
	public void onStart()
	{
		mVisible = true;
		mLifecycle.onStart();
	}

	@Override
	public void onResume()
	{
		mResumed = true;
		mLifecycle.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
	}

	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode)
	{
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
	}

	@Override
	public void onPause()
	{
		mResumed = false;
		mLifecycle.onPause();
	}

	@Override
	public void onStop()
	{
		mVisible = false;
		mLifecycle.onStop();
	}

	@Override
	public void onDestroyView()
	{
		mInLayout = false;
		if (getViewGroup() != null)
			getViewGroup().removeView(getView());//获取根布局，移出fragment布局
		if (!mRetainInstance)
		{
			mAdded = false;
			view = null;//确认不保存fragment后将其view释放
		}
		//root = null;//取消对根部局的引用，避免内存泄露
	}

	@Override
	public void onDestroy()
	{
		mLifecycle.onDestory();
		mRemoving = true;
		//getActivity().getLifecycles().remove(getClass().getCanonicalName());
	}

	@Override
	public void onDetach()
	{
		mDetached = true;
		mActivity = null;
		mPage = null;
	}

}
