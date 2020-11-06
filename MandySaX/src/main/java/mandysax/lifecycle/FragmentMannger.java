package mandysax.lifecycle;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;

public final class FragmentMannger implements FragmentManngerImpl
{

	//进度：viewgroup这里
	//进度：做动画，似乎要解析xml？？？
    //进度：返回栈
    //ok

    private final ArrayList<Fragment> fragments;//这个hashmap可以保证不会添加空的fragment(已删除)

	private Bundle savedInstanceState;

	private FragmentActivity context;//不可final是因为此处可能会内存泄露
	//用Activity而不是Context是因为
    //它一定是Activity
    //让他为Activity
    //可以使用更多的功能

	private Fragment oldFragment;

    public FragmentMannger(FragmentActivity context)
	{
        this.context = context;
        fragments = new ArrayList<Fragment>();
    }

	@Override
    public FragmentMannger add(int id, Fragment fragment)
	{
		add(id, fragment, null);
		return this;
    }

	@Override
    public FragmentMannger add(int id, Fragment fragment, Object tag)
	{	
		//start construct fragment lifecycle
		fragment.onAttach(context);
		fragment.onCreate(savedInstanceState);
		//this.fragment.onStart();
		//没有show()不应该走start周期
		fragment.setViewGroupId(id);
		if (!fragment.isInLayout())
		{
			final View view1=fragment.onCreateView(LayoutInflater.from(context), fragment.getViewGroup(), savedInstanceState);
			final View view2=fragment.onCreateView(LayoutInflater.from(context), fragment.getViewGroup());
			if (view1 != null || view2 != null)
				fragment.onViewCreated(view1 == null ?view2: view1, savedInstanceState);
			fragment.onActivityCreated(savedInstanceState);
		}
		else
		{
			fragment.onViewCreated(fragment.getView(), savedInstanceState);	
		}
		fragment.setTag(tag == null ?fragment.getClass().getCanonicalName(): tag.toString());
		if (findFragmentByTag(fragment.getTag()) == null)
			fragments.add(fragment);
		oldFragment = fragment;
        return this;
    }

	@Override
	public FragmentMannger show(Fragment fragment)
	{   
		if (fragment == null)return this;//省事
		if (fragment.isHidden())
		{
			fragment.onStart();
			fragment.onResume();	
			//走生命周期
		    fragment.getView().setVisibility(View.VISIBLE);
		}
		return this;
	}

	@Override
	public FragmentMannger hide(Fragment fragment)
	{
		if (fragment == null)return this;
		//一样，省事
		if (fragment.isVisible())
		{
			fragment.getView().setVisibility(View.GONE);
			fragment.onPause();
			fragment.onStop();			
			//fragment.onDestroyView();
		}
		return this;
	}

	@Override
	public FragmentMannger replace(int id, Class replaceFragment)
	{
		return replace(id, replaceFragment, null);
	}

	@Override
	public FragmentMannger replace(int id, Class replaceFragment, Object tag)
	{
		if (replaceFragment == null)return null;

		//替换fragment
		//emmmm，有些简单粗暴，但我。。。懒
		try
		{
			final Object object=replaceFragment.newInstance();
			if (object instanceof Fragment)
			{
				oldFragment = (Fragment) object;
				//oldFragment.setRetainInstance(false);
				for (Fragment fragment:fragments)
				{
					if (fragment.getViewGroup() != null)
						if (fragment.getViewGroup().getId() == id)
						{
							if (fragment.isVisible())
								hide(fragment);
						}
				}
				add(id, oldFragment, tag).show(oldFragment);
				return this;
			}
			else throw new ClassCastException(replaceFragment.getCanonicalName() + " is not the type of fragment");//懂的都懂
		}
		catch (Exception e)
		{
			throw new NullPointerException("Failed to load fragment is " + replaceFragment.getCanonicalName());
			//加载失败了，兄弟萌
		}
		//已做
		//这里添加fragment，还没做 20.10.5下午三点三分
	}

	/*
	 @Override//原生的fragment添加返回栈还要一个描述，感觉好像没啥用，就删了
	 public FragmentMannger addToBackStack()
	 {
	 //添加返回栈
	 //还没做
	 return this;
	 }
	 */

	@Override
    public Fragment findFragmentByTag(Object tag)
	{
		for (Fragment fragment:fragments)
		{
			if (tag.toString().equals(fragment.getTag().toString()))	
				return fragment;
		}
		return null;
    }

	@Override
	public FragmentMannger remove(Fragment fragment)
	{
		if (!fragment.getRetainInstance())
		{
			fragments.remove(fragment);
		}
		fragment.onDestroyView();
		fragment.onDestroy();
		fragment.onDetach();	
		return this;
	}

	@Override
	public FragmentMannger addToBackStack()
	{
		context.addToBackStack(oldFragment);
		return this;
	}

	/*
	 生命周期
	 */;

	void resumeFragment()
	{
		for (Fragment one:fragments)
		{
			if (one.isAdded() && one.isInLayout())
			{
				if (one.isVisible())
				{
					one.onViewCreated(one.getView(), savedInstanceState);
					show(one);
				}
				else
				{
					one.onViewCreated(one.getView(), savedInstanceState);
				}
			}
		}
	}

	void create(FragmentActivity activity, Bundle activitySavedInstanceState)
	{
		context = activity;
		savedInstanceState = activitySavedInstanceState;
		for (Fragment one:fragments)
				one.onAttach(context);
	}
	
	protected void saveInstanceState(Bundle outState)
	{
		for (Fragment fragment:fragments)
			fragment.onSaveInstanceState(outState);
	}

	protected void multiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
		for (Fragment fragment:fragments)
			fragment.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
   	}

	protected void multiWindowModeChanged(boolean isInMultiWindowMode)
	{
		for (Fragment fragment:fragments)
			fragment.onMultiWindowModeChanged(isInMultiWindowMode);
	}

	
	protected void configurationChanged(Configuration newConfig)
	{
		for (Fragment fragment:fragments)
			fragment.onConfigurationChanged(newConfig);
	}
	
	protected void start()
	{
		for (Fragment one:fragments)
		{
			/*
             //应该在此恢复fragment到原布局上
             if (one.isAdded())
             {
             //make
             }
             */
			//if (one.isVisible())
			one.onStart();
        }
	}

	protected void resume()
	{
		for (Fragment one:fragments)
		//if (one.isVisible())
			one.onResume();
	}

	protected void pause()
	{
		for (Fragment one:fragments)
		//if (one.isVisible())
			one.onPause();
	}

	protected void stop()
	{
		for (Fragment one:fragments)
		//if (one.isVisible())
			one.onStop();
	}

   protected void destroy()
	{
		for (Fragment one:fragments)
			remove(one);
        savedInstanceState = null;
		oldFragment = null;
		context = null;
    }
 
	protected void clear()
	{
		fragments.clear();
	}
	/*
	 生命周期
	 */
}
