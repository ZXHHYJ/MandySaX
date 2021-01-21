package mandysax.plus.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FragmentMannger implements FragmentManngerImpl
{

	//进度：viewgroup这里
	//进度：做动画，似乎要解析xml？？？
    //进度：返回栈
    //ok

    //private final ArrayList<SaveFragment> fragments;//这个hashmap可以保证不会添加空的fragment(已删除)

    private final ConcurrentHashMap<Object,Fragment> fragments;

	private Bundle savedInstanceState;

	private FragmentActivity context;
    //不可final是因为此处可能会内存泄露
	//用Activity而不是Context是因为
    //它一定是Activity
    //让ta为Activity
    //可以使用更多的功能
    //最后一次修改于2021.1.1 12:58

	//private Fragment oldFragment;

    public FragmentMannger(FragmentActivity context)
	{
        this.context = context;
        fragments = new ConcurrentHashMap<Object,Fragment>();
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
			View view1 = fragment.onCreateView(LayoutInflater.from(context), fragment.getViewGroup(), savedInstanceState);
            View view2 = null;
            if (view1 == null)
                view2 = fragment.onCreateView(LayoutInflater.from(context), fragment.getViewGroup());
            fragment.onViewCreated(view1 == null ?view2: view1, savedInstanceState);
			fragment.onActivityCreated(savedInstanceState);
		}
		else
		{
			fragment.onViewCreated(fragment.getView(), savedInstanceState);	
		}
		Object tag2=tag == null ?fragment.getClass().getCanonicalName(): tag;
		//if (findFragmentByTag(tag2) == null)
		fragments.put(tag2, fragment);
		//oldFragment = fragment;
        return this;
    }

	@Override
	public FragmentMannger show(Fragment fragment)
	{   
		if (fragment == null)return this;//省事
		if (fragment.isHidden())
		{
			fragment.getView().setVisibility(View.VISIBLE);
			fragment.onStart();
			fragment.onResume();	
			//走生命周期
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
		return replace(id, replaceFragment, replaceFragment.getCanonicalName());
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
				Fragment fragment = (Fragment) object;
				//oldFragment.setRetainInstance(false);
                for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
                {
                    Fragment fragment2=entry.getValue();
                    if (fragment2.getViewGroup() != null)
                        if (fragment2.getViewGroup().getId() == id)
                        {
                            if (fragment2.isVisible())
                                hide(fragment2);
						}
                }

				add(id, fragment, tag).show(fragment);
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
		return fragments.get(tag);
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

	/*@Override
     public FragmentMannger addToBackStack()
     {
     context.addToBackStack(oldFragment);
     return this;
     }*/
    //于2021.1.7日废弃

	/*
	 生命周期
	 */;

	protected void resumeFragment()
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			if (fragment.isAdded() && fragment.isInLayout())
			{
				if (fragment.isVisible())
				{
					fragment.onViewCreated(fragment.getView(), savedInstanceState);
					show(fragment);
				}
				else
				{
					fragment.onViewCreated(fragment.getView(), savedInstanceState);
				}
			}
		}
	}

	protected void create(FragmentActivity activity, Bundle activitySavedInstanceState)
	{
		context = activity;
		savedInstanceState = activitySavedInstanceState;
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onAttach(context);
		}
	}

	protected void saveInstanceState(Bundle outState)
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onSaveInstanceState(outState);
		}
	}

	protected void multiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig)
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
		}
   	}

	protected void multiWindowModeChanged(boolean isInMultiWindowMode)
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onMultiWindowModeChanged(isInMultiWindowMode);
		}
	}


	protected void configurationChanged(Configuration newConfig)
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onConfigurationChanged(newConfig);
		}
	}

	protected void start()
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			/*
             //应该在此恢复fragment到原布局上
             if (one.isAdded())
             {
             //make
             }
             */
			//if (one.isVisible())
			fragment.onStart();
        }
	}

	protected void resume()
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onResume();
		}
	}

	protected void pause()
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onPause();
		}
	}

	protected void stop()
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			fragment.onStop();
		}
	}

	protected void destroy()
	{
		for (Map.Entry<Object,Fragment> entry:fragments.entrySet())
        {
            Fragment fragment=entry.getValue();
			remove(fragment);
		}
        savedInstanceState = null;
		//oldFragment = null;
		context = null;
    }

	protected void clear()
	{
		fragments.clear();
	}
	/*
	 生命周期
	 */

	/*private static class SaveFragment
     {
     public Fragment fragment;
     public String tag;

     public SaveFragment(Fragment fragment, String tag)
     {
     this.fragment = fragment;
     this.tag = tag;
     }
     }*/

}
