package mandysax.design.fragmentpage.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.util.ArrayList;
import mandysax.plus.fragment.Fragment;
import mandysax.plus.fragment.FragmentActivityImpl;
import mandysax.plus.fragment.FragmentController2Impl;

public class FragmentPage extends FrameLayout
{

	//管理器
	private final ArrayList<Fragment> mFragmentList=new ArrayList<>();

	//之前的下标
	private int mLastIndex=-1;

	//当前下标
	private int mIndex;

	//fragment管理器
	private FragmentController2Impl mMannger;

	public FragmentPage(Context p0)
	{
		super(p0);
	}

	public FragmentPage(Context p0, AttributeSet p1)
	{
		super(p0, p1);
	}

	/*
	 *添加fragment到显示列表
	 */
	public FragmentPage add(Class... fragmentClass)
	{
		for (Class className:fragmentClass)
		{
			Fragment getFragment=getMannger().findFragmentByTag(className.getCanonicalName());
			if (getFragment == null)
				try
				{
					Fragment fragment=(Fragment)className.newInstance();
					getMannger().add(getId(), fragment).commit();
					mFragmentList.add(fragment);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e.getMessage());
				}
			else mFragmentList.add(getFragment);
		}
		return this;
	}

	/*
	 *获取当前显示Fragment下标
	 */
	public int getIndex()
	{
		return mIndex;
	}

	/*
	 *显示指定下标的Fragment
	 */
	public void showFragment(int index)
	{
		this.mIndex = index;
		if (mLastIndex != -1)
			getMannger().hide(mFragmentList.get(mLastIndex)).commit();
		getMannger().show(mFragmentList.get(index)).commit();
		mLastIndex = index;
	}

	private FragmentController2Impl getMannger()
	{
		return mMannger == null ?mMannger = ((FragmentActivityImpl)getContext()).getFragmentPlusManager(): mMannger;
	}

}
