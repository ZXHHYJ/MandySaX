package mandysax.design.fragmentpage.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.util.ArrayList;
import mandysax.plus.fragment.Fragment;
import mandysax.plus.fragment.FragmentActivity;
import mandysax.plus.fragment.FragmentMannger;

public class FragmentPage extends FrameLayout
{

	public final static String TAG=FragmentPage.class.getCanonicalName();

	private final ArrayList<Class> classList=new ArrayList<Class>();

	private int oldIndex=-1;

	private int index;
	
	private FragmentMannger mMannger;

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
		if (getContext() instanceof FragmentActivity)
		{
			pageFragment = (PageFragment) getMannger().findFragmentByTag(TAG);
			if (pageFragment == null)
			{
				getMannger().add(getId(), pageFragment = new PageFragment(), TAG);
			}
			else
			{
				oldIndex = pageFragment.index;
			}
		}
		else
		{
			throw new RuntimeException("this activity is not a subclass of FragmentActivity");
		}
	}

	public FragmentPage add(Class... fragmentClass)
	{
		if (fragmentClass != null)
			if (classList.isEmpty())
				for (Class className:fragmentClass)
				{
					classList.add(className);
				}
				return this;
	}

	public int getIndex(){
		return index;
	}
	
	public void showFragment(int index)
	{
		this.index=index;
		if (getMannger().findFragmentByTag(index - 999) == null)
		{
			getMannger().replace(getId(), classList.get(index), index - 999);
		}
		else
		{
			getMannger()
				.hide(getMannger().findFragmentByTag(oldIndex - 999))
				.show(getMannger().findFragmentByTag(index - 999));
		}
		pageFragment.index = oldIndex = index;
	}

	private FragmentMannger getMannger()
	{
		return mMannger == null ?mMannger = ((FragmentActivity)getContext()).getMannger(): mMannger;
	}

}class PageFragment extends Fragment
{
	public int index=-1;
}
