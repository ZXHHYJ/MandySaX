package mandysax.fragmentpage.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.util.ArrayList;
import mandysax.lifecycle.Fragment;
import mandysax.lifecycle.FragmentActivity;
import mandysax.lifecycle.FragmentMannger;

public class FragmentPage extends FrameLayout
{

	public final static String TAG="封 茗 囧 菌";

	private final ArrayList<Class> classList=new ArrayList<Class>();

	private int oldIndex=-1;

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

	public void add(Class... fragmentClass)
	{
		if (fragmentClass != null)
			if (classList.isEmpty())
				for (Class className:fragmentClass)
				{
					classList.add(className);
				}
	}

	public void showFragment(int index)
	{
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
