package mandysax.lifecycle;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import java.util.Collection;
import mandysax.data.SafetyHashMap;

public final class FragmentMannger implements FragmentManngerImpl
{

	//进度：viewgroup这里
	//进度：做动画，似乎要解析xml？？？
    //进度：返回栈

    private final SafetyHashMap<Object,Fragment> fragments;//这个hashmap可以保证不会添加空的fragment

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
        fragments = new SafetyHashMap<Object,Fragment>();
    }

	//感觉不应该用public此类
	Collection<Fragment> getFragments()
	{
		return fragments.values();
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
		//构建fragment生命周期
		//	if (fragments.get(fragment.getClass()) != null) return this;
		fragment.onAttach(context);
		fragment.onCreate(savedInstanceState);//后面做
		//this.fragment.onStart();
		//没有show()不应该走start周期
		fragment.setViewGroupId(id);
		if (tag == null)
		{
			fragments.put(fragment.getClass(), fragment);
		}
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
		    if (fragment.getView() == null)
			{
				final View view1=fragment.onCreateView(LayoutInflater.from(context), fragment.getViewGroup(), savedInstanceState);
				final View view2=fragment.onCreateView(LayoutInflater.from(context), fragment.getViewGroup());
				fragment.onViewCreated(view1 == null ?view2: view1, savedInstanceState);		
			}
			else
			if (!fragment.isInLayout())
				fragment.onViewCreated(fragment.getView(), savedInstanceState);					
			if (context != null)
				fragment.onActivityCreated(savedInstanceState);
		}
		return this;
	}

	@Override
	public FragmentMannger hide(Fragment fragment)
	{
		if (fragment == null)return this;
		//一样，省事
		if (fragment.isAdded())
			if (fragment.getViewGroup() != null)
			{
				fragment.getViewGroup().removeView(fragment.getView());
				fragment.onPause();
				fragment.onStop();			
				fragment.onDestroyView();
			}
		return this;
	}

	@Override
	public FragmentMannger replace(int id, Class replaceFragment)
	{
		if (replaceFragment == null)return null;

		//替换fragment
		//emmmm，有些简单粗暴，但我。。。懒
		try
		{
			final Object object=replaceFragment.newInstance();
			if ( object instanceof Fragment)
			{
				final Fragment fragment=(Fragment) object;
				//fragment.setRetainInstance(false);
				add(id, fragment);
				show(fragment);
				oldFragment = fragment;
				return this;
			}
			else throw new ClassCastException(replaceFragment.getCanonicalName() + " is not the type of fragment");//懂的都懂
		}
		catch (Exception e)
		{
			throw new NullPointerException("Failed to load fragment:" + replaceFragment.getCanonicalName());
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
		if(fragment==null)return this;
		//销毁fragment的生命周期
		//确认是否保存
		if (!fragment.getRetainInstance())fragments.remove(fragments.getClass());
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
	 */

	void resumeFragment()
	{
		for (Fragment one:fragments.values())
		{
			if (one.isAdded())
			{
				context.getMannger().show(one);
			}
		}
	}

	void create(FragmentActivity activity, Bundle activitySavedInstanceState)
	{
		context = activity;
		savedInstanceState = activitySavedInstanceState;
		for (Fragment one:fragments.values())
			if (one.isDetached())
				one.onAttach(context);
	}

	void start()
	{
		for (Fragment one:fragments.values())
		{
			/*
			//应该在此恢复fragment到原布局上
			if (one.isAdded())
			{
				//make
			}
			*/
			if (one.isVisible())
				one.onStart();
        }
	}

	void resume()
	{
		for (Fragment one:fragments.values())
			if (one.isVisible())
				one.onResume();
	}

	void pause()
	{
		for (Fragment one:fragments.values())
			if (one.isVisible())
				one.onPause();
	}

	void stop()
	{
		for (Fragment one:fragments.values())
			if (one.isVisible())
				one.onStop();
	}

    void destroy()
	{
		for (Fragment one:fragments.values())
			remove(one);
		oldFragment = null;
		context = null;
    }

	void clear()
	{
		fragments.clear();
	}
	/*
	 生命周期
	 */
}
