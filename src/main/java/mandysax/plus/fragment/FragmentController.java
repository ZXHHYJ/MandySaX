package mandysax.plus.fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleObserver;
import mandysax.plus.transaction.Transaction;
import mandysax.utils.Log.LogUtils;

public final class FragmentController implements FragmentControllerImpl
{

	/*
	 *将对Fragment的各种操作都放在FragmentController中
	 *FragmentController2相当于起了记录作用
	 *将各种操作从之前的FeagmentManager中分离
	 */
	public class FragmentController2 implements FragmentController2Impl
	{

		private final Transaction<FragmentHash>.TransactionManager<FragmentHash> mManager=mHash.beginTransaction();//默认直接开启事务，免去这个繁琐步骤

		private int mStartAnim,mExitAnim,mStartAnim2,mExitAnim2;

		private boolean mIsAddBackStack=false;//记录有没有添加到返回栈

		private Fragment mLeftFragment;
		/*
		 *这个变量用于记录add操作时的Fragment
		 *系统Fragment在这个步骤默认已经向容器添加了Fragment而我并没有这么做，这样在add后的show(Fragment fragment)操作繁琐，就添加了无参的show()
		 *这样能更好的控制Fragment，因为有的Fragment并没有界面，默认添加到容器有些限制Fragment的操作上限
		 */

		private FragmentHash initFragmentHash(Fragment fragment, STACK stark/*执行的操作*/ , String tag, int id)
		{
			FragmentHash hash=new FragmentHash(fragment, tag, stark, id, stark == STACK.SHOW ?mStartAnim: stark == STACK.HIDE ?mStartAnim2: 0, stark == STACK.SHOW ?mExitAnim: stark == STACK.HIDE ?mExitAnim2: 0);
			mManager.putData(hash);
			return hash;
		}

		@Override
		public Fragment findFragmentByTag(String tag)
		{
			return getFragmentManager().tagGetFragment(tag);
		}

		@Override
		public FragmentController2 setCustomAnimations(int startAnim/*show 开始动画*/, int exitAnim/*show 结束动画*/, int startAnim2/*hide开始动画*/, int exitAnim2)
		{
			mStartAnim = startAnim;
			mExitAnim = exitAnim;
			mStartAnim2 = startAnim2;
			mExitAnim2 = exitAnim2;
			return this;
		}

		@Override
		public FragmentController2 add(int id, Fragment fragment)
		{
			return add(id, fragment, null);
		}

		@Override
		public FragmentController2 add(int id, Fragment fragment, String tag)
		{
			mLeftFragment = fragment;
			initFragmentHash(fragment, STACK.ADD, tag, id);
			return this;
		}

		@Override
		public FragmentController2 remove(Fragment fragment)
		{
			initFragmentHash(fragment, STACK.REMOVE, null, 0);
			return this;
		}

		@Override
		public FragmentController2 show(Fragment fragment)
		{
			initFragmentHash(fragment, STACK.SHOW, null, 0);
			return this;
		}

		@Override
		public FragmentController2 show()
		{
			if (mLeftFragment == null)throw new NullPointerException("There is no add() operation before show()");
			show(mLeftFragment);
			return this;
		}

		@Override
		public FragmentController2 hide(Fragment fragment)
		{
			initFragmentHash(fragment, STACK.HIDE, null, 0);
			return this;
		}

		@Override
		public FragmentController2 replace(int id, Class replaceFragment)
		{
			return replace(id, replaceFragment, replaceFragment.getCanonicalName());
		}

		@Override
		public FragmentController2 replace(int id, Class replaceFragment, String tag)
		{
			try
			{
				for (Fragment fragment2:getFragmentManager().getFragments())
					if (fragment2.getViewGroup() != null && fragment2.getViewGroup().getId() == id)
					{
						if (fragment2.isVisible())
							hide(fragment2);		
					}
				Fragment fragment=(Fragment)replaceFragment.newInstance();
				add(id, fragment, tag).show();
			}
			catch (IllegalAccessException e)
			{}
			catch (InstantiationException e)
			{}
			return this;
		}

		public FragmentController2 addToBackStack()
		{
			mIsAddBackStack = true;
			commit();
			return this;
		}

		public void commit()
		{
			if (mIsAddBackStack == true)
			{
				mManager.commit();
			}
			else
			{
				mManager.chancel();
			}
		}

	}

	private final FragmentManager mManager;

	private Bundle mSavedInstanceState;

	private FragmentActivity mActivity;

	public FragmentController(FragmentActivity actvity)
	{
		mManager = new FragmentManager();
		mActivity = actvity;
	}

	public enum STACK
	{
		SHOW,
		HIDE,
		REMOVE,
		ADD;
	}

	public class FragmentHash
	{
		final Fragment fragment;
		final String tag;
		final STACK stack;
		final int id,startAnim,exitAnim;

		public FragmentHash(Fragment fragment, String tag, STACK stack, int id, int startAnim, int exitAnim)
		{
			this.fragment = fragment;
			this.tag = tag;
			this.stack = stack;
			this.id = id;
			this.startAnim = startAnim;
			this.exitAnim = exitAnim;
		}	

	}

	private Transaction<FragmentHash> mHash=new Transaction<FragmentHash>().setOnUpDatahListener(new Transaction.OnUpDatahListener<FragmentHash>(){

			@Override
			public void chancelData(FragmentController.FragmentHash data)
			{
				/*默认操作*/
				initHash(data);
			}

			@Override
			public void commitData(FragmentController.FragmentHash data)
			{
				/*添加返回栈会走这里*/
				initHash(data);
			}

			@Override
			public void backData(FragmentController.FragmentHash data)
			{
				/*回退后的*/
				if (data.stack == STACK.REMOVE)return;
				if (data.stack == STACK.ADD)return;
				initHash(new FragmentHash(data.fragment, data.tag, data.stack == STACK.SHOW ?STACK.HIDE: STACK.SHOW, data.id, data.startAnim, data.exitAnim));
			}

		});

	private void initHash(final FragmentHash hash)
	{
		LogUtils.w(getClass(), hash.stack + " " + hash.fragment);
		switch (hash.stack)
		{
			case SHOW:
				if (hash.fragment.isAdded() && hash.fragment.isHidden())
				{
					/*
					 *在2.0.0中，Fragment实现了ViewStub延迟加载
					 */
					if (hash.fragment.getView() != null)
					{
						hash.fragment.getView().setVisibility(View.VISIBLE);
					}
					else return;
					if (hash.startAnim != 0)
					{
						Animation startAnim=AnimationUtils.loadAnimation(mActivity, hash.startAnim);
						startAnim.setAnimationListener(new Animation.AnimationListener(){

								@Override
								public void onAnimationStart(Animation p1)
								{
									hash.fragment.onStart();
								}

								@Override
								public void onAnimationEnd(Animation p1)
								{
									hash.fragment.onResume();	
								}

								@Override
								public void onAnimationRepeat(Animation p1)
								{
								}
							});
						hash.fragment.getView().startAnimation(startAnim);	
					}	
				}
				break;
			case HIDE:
				if (hash.fragment.isAdded() && hash.fragment.isVisible())
				{
					if (hash.fragment.getView() != null)
					{
						if (hash.exitAnim != 0)
						{
							Animation exitAnim=AnimationUtils.loadAnimation(mActivity, hash.exitAnim);
							exitAnim.setAnimationListener(new Animation.AnimationListener(){

									@Override
									public void onAnimationStart(Animation p1)
									{
										hash.fragment.onPause();
									}

									@Override
									public void onAnimationEnd(Animation p1)
									{
										hash.fragment.getView().setVisibility(View.GONE);
										hash.fragment.onStop();
									}

									@Override
									public void onAnimationRepeat(Animation p1)
									{
									}
								});
							hash.fragment.getView().startAnimation(exitAnim);
						}
						else
						{
							hash.fragment.getView().setVisibility(View.GONE);
							hash.fragment.onPause();
							hash.fragment.onStop();
						}
					}
				}
				break;
			case REMOVE:
				if (!hash.fragment.getRetainInstance())
				{
					getFragmentManager().removeFragment(hash.fragment);
				}
				hash.fragment.onDestroyView();
				hash.fragment.onDestroy();
				hash.fragment.onDetach();
				break;
			case ADD:
				if (hash.fragment.isAdded())return;
				String tag=hash.tag == null ?hash.fragment.getClass().getCanonicalName(): hash.tag;
				getFragmentManager().addFragment(hash.fragment, tag);
				hash.fragment.setTag(tag, hash.id);
				hash.fragment.onAttach(mActivity);
				hash.fragment.onCreate(mSavedInstanceState);
				if (!hash.fragment.isInLayout())
				{
					View view1 = null;
					if (hash.fragment.onCreateView() != 0)
						view1 = hash.fragment.getLayoutInflater().inflate(hash.fragment.onCreateView(), hash.fragment.getViewGroup(), false); 
					View view2=hash.fragment.onCreateView(LayoutInflater.from(mActivity), hash.fragment.getViewGroup());
					hash.fragment.onViewCreated(view1 == null ?view2: view1, mSavedInstanceState);
					hash.fragment.onActivityCreated(mSavedInstanceState);
				}
				break;
		}
	}

	private FragmentManager getFragmentManager()
	{
		return mManager;
	}

	@Override
	public boolean onBackFragment()
	{
		return mHash.rollback();
	}

	@Override
	public FragmentController2 getFragmentController2()
	{
		return new FragmentController2();
	}

	/*
	 *控制器的生命周期
	 */;

	@Override
	public void resumeFragment()
	{
		for (Fragment fragment:getFragmentManager().getFragments())
		{
			if (fragment.isAdded() && fragment.isInLayout())
			{
				if (fragment.getView() != null && fragment.getViewGroup() != null)
					fragment.getViewGroup().addView(fragment.getView());
				else return;
				if (fragment.isVisible())			
					getFragmentController2().show(fragment).commit();		
			}
			fragment.onActivityCreated(mSavedInstanceState);
		}
	}

	@Override
	public void create(final FragmentActivity activity, Bundle activitySavedInstanceState)
	{
		mActivity = activity;
		mSavedInstanceState = activitySavedInstanceState;
		for (Fragment fragment:getFragmentManager().getFragments())
		{
			fragment.onAttach(mActivity);
		}
		activity.getLifecycle().addObsever(new LifecycleObserver(){
				@Override
				public void Observer(String State)
				{
					for (Fragment fragment:getFragmentManager().getFragments())
					{
						//只有显示的Fragment才可以接收到生命周期事件
						switch (State)
						{
							case Lifecycle.Event.ON_START:
								if (fragment.isVisible())
									fragment.onStart();
								break;
							case Lifecycle.Event.ON_RESUME:
								if (fragment.isVisible())
									fragment.onResume();
								break;
							case Lifecycle.Event.ON_PAUSE:
								if (fragment.isVisible())
									fragment.onPause();
								break;
							case Lifecycle.Event.ON_STOP:
								if (fragment.isVisible())
									fragment.onStop();
								break;
							case Lifecycle.Event.ON_DESTORY:
								getFragmentController2().remove(fragment).commit();
								if (!getFragmentManager().getFragments().isEmpty() && !activity.isChangingConfigurations())
								{
									getFragmentManager().getFragments().clear();
								}
								mSavedInstanceState = null;
								mActivity = null;
								break;
						}
					}
				}
			});
	}

	@Override
	public void saveInstanceState(Bundle outState)
	{
		for (Fragment fragment:getFragmentManager().getFragments())
		{
			fragment.onSaveInstanceState(outState);
		}
	}

	@Override
	public void multiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
		for (Fragment fragment:getFragmentManager().getFragments())
		{
			fragment.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
		}
	}

	@Override
	public void multiWindowModeChanged(boolean isInMultiWindowMode)
	{
		for (Fragment fragment:getFragmentManager().getFragments())
		{
			fragment.onMultiWindowModeChanged(isInMultiWindowMode);
		}
	}

	@Override
	public void configurationChanged(Configuration newConfig)
	{
		for (Fragment fragment:getFragmentManager().getFragments())
		{
			fragment.onConfigurationChanged(newConfig);
		}
	}

	/*
	 *控制器的生命周期
	 */
}
