package mandysax.plus.fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleObserver;
import mandysax.plus.lifecycle.livedata.MutableLiveData;
import mandysax.plus.lifecycle.livedata.Observer;

public final class FragmentController implements FragmentControllerImpl,LifecycleObserver
{

	@Override
	public void Observer(String State)
	{
		if (mActivityCreated.getValue() == null)
			if (State == Lifecycle.Event.ON_START)		
				mActivityCreated.setValue(true);	
	}

	/*
	 *将对Fragment的各种操作都放在FragmentController中
	 *FragmentController2相当于起了记录作用
	 *将各种操作从之前的FeagmentManager中分离
	 */
	public interface OnBackStackChangedListener
	{
		public void onBackStackChanged()
	}

	enum STACK
	{
		SHOW,
		HIDE,
		REMOVE,
		REPLACE,
		ADD;
	}

	final class Op
	{
		int id;
		String tag;
		STACK stack;
		Fragment fragment;
		int enterAnim;
		int exitAnim;
		int popEnterAnim;
		int popExitAnim;
		boolean isAddToBackStack;
		ArrayList<Fragment> removed;
	}

	public class BackStackRecord
	{

		private final List<Op> mActive;

		public BackStackRecord(List<Op> opl)
		{
			mActive = opl;
		}

		public Fragment get(int index)
		{
			return mActive.get(index).fragment;
		}

		public int size()
		{
			return mActive.size();
		}

		public void run()
		{
			/*run*/
			for (Op op:mActive)
			{
				moveToStack(op);
			}		
		}

		public void rollback()
		{
			/*rollback*/
			for (final Op op:mActive)
			{
				if (op.stack == STACK.REMOVE)return;
				if (op.stack == STACK.ADD)return;
				if (op.stack == STACK.SHOW)
				{
					hide(op.fragment, op.popExitAnim);
					return;
				}
				if (op.stack == STACK.HIDE)
				{
					show(op.fragment, op.popEnterAnim);
					return;
				}
				if (op.stack == STACK.REPLACE && op.isAddToBackStack)
				{
					op.fragment.getLifecycle().addObsever(new LifecycleObserver(){

							@Override
							public void Observer(String State)
							{
								if (State == Lifecycle.Event.ON_STOP && op.fragment.isHidden())
								{
									remove(op.fragment);
								}
							}			
						});
					hide(op.fragment, op.popExitAnim);
					for (Fragment fragment:op.removed)
					{
						show(fragment, op.enterAnim);
					}		
				}
			}		
		}

		public void moveToStack(Op op)
		{
			switch (op.stack)
			{
				case ADD:
					add(op.fragment, op.id, op.tag);
					break;
				case SHOW:
					show(op.fragment, op.enterAnim);
					break;
				case HIDE:
					hide(op.fragment, op.exitAnim);
					break;
				case REPLACE:
					replace(op);
					break;
				case REMOVE:
					remove(op.fragment);
					break;
			}
		}

		public void replace(Op op)
		{
			for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
			{
				final Fragment fragment2=entry.getValue();
				if (fragment2.getViewGroup() != null && fragment2.getViewGroup().getId() == op.id)
				{
					if (fragment2.isVisible())
					{
						if (op.removed == null)
							op.removed = new ArrayList<Fragment>();
						op.removed.add(fragment2);
						if (!op.isAddToBackStack)
						{			
							fragment2.getLifecycle().addObsever(new LifecycleObserver(){

									@Override
									public void Observer(String State)
									{	
										if (State == Lifecycle.Event.ON_STOP && fragment2.isHidden())
										{
											remove(fragment2);
										}
									}			
								});
						}
						hide(fragment2, op.exitAnim);
					}
				}
			}
			add(op.fragment, op.id, op.tag);
			show(op.fragment, op.popEnterAnim);
		}

		public void add(final Fragment fragment, int id, String tag)
		{
			tag = tag == null ?fragment.getClass().getCanonicalName(): tag;
			if (fragment.isAdded())
			{
				fragment.setTag(tag, id);
			}
			else
			{
				getFragmentManager().addFragment(fragment, tag);
				fragment.setTag(tag, id);
				fragment.onAttach(mActivity);
				fragment.onCreate(mSavedInstanceState);
			}
			if (!fragment.isInLayout() && id != 0)
			{
				View view1 = null;
				if (fragment.onCreateView() != 0)
					view1 = fragment.getLayoutInflater().inflate(fragment.onCreateView(), fragment.getViewGroup(), false); 
				View view2=fragment.onCreateView(LayoutInflater.from(mActivity), fragment.getViewGroup());
				fragment.onViewCreated(view1 == null ?view2: view1, mSavedInstanceState);
				mActivityCreated.apply(new Observer<Boolean>(){

						@Override
						public void onChanged(Boolean p1)
						{
							fragment.onActivityCreated(mSavedInstanceState);
						}
					});
			}
		}

		public void remove(Fragment fragment)
		{
			getFragmentManager().removeFragment(fragment);
			fragment.onDestroyView();
			fragment.onDestroy();
			fragment.onDetach();
		}

		public void show(final Fragment fragment, int anim)
		{
			if (fragment.isAdded() && fragment.isHidden())
				if (fragment.getView() != null)
				{
					fragment.getView().setVisibility(View.VISIBLE);
					if (anim != 0)
					{
						Animation startAnim=AnimationUtils.loadAnimation(mActivity, anim);
						startAnim.setAnimationListener(new Animation.AnimationListener(){

								@Override
								public void onAnimationStart(Animation p1)
								{
									fragment.onStart();
								}

								@Override
								public void onAnimationEnd(Animation p1)
								{
									fragment.onResume();	
								}

								@Override
								public void onAnimationRepeat(Animation p1)
								{
								}
							});
						fragment.getView().startAnimation(startAnim);	
					}	
				}
		}

		public void hide(final Fragment fragment, int anim)
		{
			if (fragment.isAdded() && fragment.isVisible())
			{
				if (fragment.getView() != null)
				{
					if (anim != 0)
					{
						Animation exitAnim=AnimationUtils.loadAnimation(mActivity, anim);
						exitAnim.setAnimationListener(new Animation.AnimationListener(){

								@Override
								public void onAnimationStart(Animation p1)
								{
									fragment.onPause();
								}

								@Override
								public void onAnimationEnd(Animation p1)
								{
									fragment.getView().setVisibility(View.GONE);
									fragment.onStop();
								}

								@Override
								public void onAnimationRepeat(Animation p1)
								{
								}
							});
						fragment.getView().startAnimation(exitAnim);
					}
					else
					{
						fragment.getView().setVisibility(View.GONE);
						fragment.onPause();
						fragment.onStop();
					}
				}
			}
		}

	}

	public class BackStack
	{

		private ArrayList<OnBackStackChangedListener> mBackl;

		private ArrayList<BackStackRecord> mBackStack;

		private ArrayList<Integer> mBackStackIndices;

	    public int allocBackStackIndex(BackStackRecord bse)
		{
			synchronized (this)
			{
				if (mBackStack == null)
					mBackStack = new ArrayList<BackStackRecord>();
				if (mBackStackIndices == null)
					mBackStackIndices = new ArrayList<>();		
				for (int i=0;i < mBackStackIndices.size();i++)
				{
					if (mBackStackIndices.get(i) == null)
					{	
						mBackStack.set(i, bse);
						mBackStackIndices.remove(i);
						return i;
					}
				}
				mBackStack.add(bse);
				mBackStackIndices.add(mBackStackIndices.size());
				return mBackStackIndices.size() - 1;
			}
		}

		public void freeBackStackIndex(int index)
		{
			mBackStack.set(index, null);
			mBackStackIndices.add(index);
		}

		public int getBackStackEntryCount()
		{
			if (mBackStack == null)return 0;	
			return mBackStack.get(mBackStack.size() - 1).size();
		}

		public Fragment getBackStackEntryAt(int index)
		{
			if (mBackStack == null)return null;
			return mBackStack.get(mBackStack.size() - 1).get(index);
		}

		public boolean popBackStack(int index)
		{
			if (mBackStack == null)return false;
			if (mBackStack.size() >= 1)
			{
				BackStackRecord bsr=mBackStack.get(index);
				if (bsr == null)return false;
				bsr.rollback();
				freeBackStackIndex(index);
				return true;
			}
			return false;
		}

		public boolean popBackStack()
		{
			if (mBackStack != null)
				return popBackStack(mBackStack.size() - 1);
			return false;
		}

		public void addOnBackStackChangedListener(OnBackStackChangedListener listener)
		{
			mBackl.add(listener);

		}

		public void removeOnBackStackChangedListener(OnBackStackChangedListener listener)
		{
			mBackl.remove(listener);
		}


	}

	private final BackStack mFragmentBackStack=new BackStack();

	public class FragmentController2 implements FragmentController2Impl
	{

		@Override
		public boolean popBackStack()
		{
			return mFragmentBackStack.popBackStack();
		}

		@Override
		public boolean popBackStack(int index)
		{
			return mFragmentBackStack.popBackStack(index);
		}

		@Override
		public int getBackStackEntryCount()
		{
			return mFragmentBackStack.getBackStackEntryCount();
		}

		@Override
		public FragmentImpl getBackStackEntryAt(int index)
		{
			return mFragmentBackStack.getBackStackEntryAt(index);
		}

		private ArrayList<Op> mOpl=new ArrayList<Op>();

		private int mIndex=0;

		@Override
		public FragmentController2Impl removeOnBackStackChangedListener(OnBackStackChangedListener listener)
		{
			mFragmentBackStack.removeOnBackStackChangedListener(listener);
			return this;
		}

		@Override
		public FragmentController2Impl addOnBackStackChangedListener(OnBackStackChangedListener listener)
		{
			mFragmentBackStack.addOnBackStackChangedListener(listener);
			return this;
		}

		private int
		mEnterAnim,
		mExitAnim,
		mPopEnterAnim,
		mPopExitAnim;

		private boolean mIsAddBackStack=false;//记录有没有添加到返回栈

		/*
		 *这个变量用于记录add操作时的Fragment
		 *系统Fragment在这个步骤默认已经向容器添加了Fragment而我并没有这么做，这样在add后的show(Fragment fragment)操作繁琐，就添加了无参的show()
		 *这样能更好的控制Fragment，因为有的Fragment并没有界面，默认添加到容器有些限制Fragment的操作上限
		 */
		public Op addOp(int id, FragmentImpl fragment, STACK stack, String tag)
		{
			Op op=new Op();
			op.id = id;
			op.fragment = (Fragment) fragment;
			op.stack = stack;
			op.tag = tag;
			op.enterAnim = mEnterAnim;
			op.exitAnim = mExitAnim;
			op.popEnterAnim = mPopEnterAnim;
			op.popExitAnim = mPopExitAnim;
			mOpl.add(op);
			return op;
		}

		@Override
		public <T extends FragmentImpl> T findFragmentByTag(String tag)
		{
			return (T)getFragmentManager().tagGetFragment(tag);
		}

		@Override
		public <T extends FragmentImpl> T findFragmentById(int id)
		{
			return findFragmentByTag(id + "");
		}

		@Override
		public FragmentController2Impl setCustomAnimations(int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim)
		{
			mEnterAnim = enterAnim;
			mExitAnim = exitAnim;
			mPopEnterAnim = popEnterAnim;
			mPopExitAnim = popExitAnim;
			return this;
		}

		@Override
		public FragmentController2Impl add(int id, FragmentImpl fragment)
		{
			return add(id, fragment, null);
		}

		@Override
		public FragmentController2Impl add(int id, FragmentImpl fragment, String tag)
		{
			addOp(id, fragment, STACK.ADD, tag);
			return this;
		}

		@Override
		public FragmentController2Impl remove(FragmentImpl fragment)
		{
			addOp(0, fragment, STACK.REMOVE, null);
			return this;
		}

		@Override
		public FragmentController2Impl show(FragmentImpl fragment)
		{
			addOp(0, fragment, STACK.SHOW, null);
			return this;
		}

		@Override
		public FragmentController2Impl hide(FragmentImpl fragment)
		{
			addOp(0, fragment, STACK.HIDE, null);
			return this;
		}

		@Override
		public FragmentController2Impl replace(int id, Class replaceFragment)
		{
			return replace(id, replaceFragment, replaceFragment.getCanonicalName());
		}

		@Override
		public FragmentController2Impl replace(int id, Class replaceFragment, String tag)
		{
			try
			{	
				addOp(id, (Fragment)replaceFragment.newInstance() , STACK.REPLACE, tag);
			}
			catch (IllegalAccessException | InstantiationException e)
			{}
			return this;
		}

		@Override
		public FragmentController2Impl addToBackStack()
		{
			mIsAddBackStack = true;
			for (int i=mIndex;i < mOpl.size();i++)
			{
				mOpl.get(i).isAddToBackStack = true;
			}
			mIndex = mOpl.size() - 1;
			return this;
		}

		@Override
		public int commit()
		{
			if (mIsAddBackStack == true)
			{
				BackStackRecord bsr=new BackStackRecord(mOpl);
				bsr.run();
				return mFragmentBackStack.allocBackStackIndex(bsr);
			}
			else
			{
				new BackStackRecord(mOpl).run();
				return -1;
			}	
		}

	}

	private final FragmentManager mManager;

	private Bundle mSavedInstanceState;

	private FragmentActivity mActivity;

	private final MutableLiveData<Boolean> mActivityCreated=new MutableLiveData<Boolean>();

	public FragmentController(FragmentActivity actvity)
	{
		mManager = new FragmentManager();
		mActivity = actvity;
		actvity.getLifecycle().addObsever(this);
	}

	private FragmentManagerImpl getFragmentManager()
	{
		return mManager;
	}

	@Override
	public boolean onBackFragment()
	{
		for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
		{
			Fragment fragment=entry.getValue();
			if (fragment.isAdded())
				if (fragment.isVisible())
					if (fragment.onBackPressed())
						return true;
		}
		return false;
	}

	@Override
	public FragmentController2Impl getFragmentController2()
	{
		return new FragmentController2();
	}

	/*
	 *控制器的生命周期
	 */;

	@Override
	public void resumeFragment()
	{
		for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
		{
			Fragment fragment=entry.getValue();
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
		for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
		{
			entry.getValue().onAttach(mActivity);
		}
		activity.getLifecycle().addObsever(new LifecycleObserver(){
				@Override
				public void Observer(String State)
				{
					for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
					{
						Fragment fragment=entry.getValue();
						//只有显示的Fragment才可以接收到生命周期事件
						switch (State)
						{
							case Lifecycle.Event.ON_START:
								if (fragment.isVisible())
									fragment.onStart();
								break;
                            case Lifecycle.Event.ON_RESTART:
                                if (fragment.isVisible())
									fragment.onRestart();
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
								fragment.onDestroyView();
								fragment.onDestroy();
								fragment.onDetach();
								mSavedInstanceState = null;
								mActivity = null;
								if (!activity.isChangingConfigurations())
								{
									getFragmentManager().clear();
									return;//stop for
								}				
								break;
						}
					}
				}
			});
	}

	@Override
	public void saveInstanceState(Bundle outState)
	{
		for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
		{
			entry.getValue().onSaveInstanceState(outState);
		}
	}

	@Override
	public void multiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
		for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
		{
			entry.getValue().onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
		}
	}

	@Override
	public void multiWindowModeChanged(boolean isInMultiWindowMode)
	{
		for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
		{
			entry.getValue().onMultiWindowModeChanged(isInMultiWindowMode);
		}
	}

	@Override
	public void configurationChanged(Configuration newConfig)
	{
		for (Map.Entry<String, Fragment> entry : getFragmentManager().entrySet())
		{
			entry.getValue().onConfigurationChanged(newConfig);
		}
	}

	/*
	 *控制器的生命周期
	 */

}
