package mandysax.design.fragmentpage.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.util.ArrayList;
import mandysax.plus.fragment.Fragment;
import mandysax.plus.fragment.FragmentActivity;
import mandysax.plus.fragment.FragmentController2Impl;

public class FragmentPage extends FrameLayout
{

	public final static String TAG=FragmentPage.class.getCanonicalName();

	//管理器
	private final ArrayList<Fragment> mFragmentList=new ArrayList<>();

	//之前的下标
	private int mLastIndex=-1;

	//当前下标
	private int mIndex;

	//fragment管理器
	private FragmentController2Impl mMannger;

	//用于存储fragment下标
	private PageFragment pageFragment;

	public FragmentPage(Context p0)
	{
		super(p0);
		init();
	}

	public FragmentPage(Context p0, AttributeSet p1)
	{
		super(p0, p1);
		init();
	}

	private void init()
	{
		//此控件只支持mandysax的activity
		if (getContext() instanceof FragmentActivity)
		{
			pageFragment = (PageFragment) getMannger().findFragmentByTag(TAG);
			if (pageFragment == null)
			{
				getMannger().add(getId(), pageFragment = new PageFragment(), TAG).commit();
			}
			else
			{
				mLastIndex = pageFragment.old_index;
				mIndex = pageFragment.index;
			}
		}
		else
		{
			throw new RuntimeException("FragmentPage activity is not a subclass of FragmentActivity");
		}
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
		pageFragment.index = mLastIndex = index;
	}

	private FragmentController2Impl getMannger()
	{
		return mMannger == null ?mMannger = ((FragmentActivity)getContext()).getFragmentPlusManager(): mMannger;
	}

}
class PageFragment extends Fragment
{
	public int old_index=-1;
	public int index=-1;
}
