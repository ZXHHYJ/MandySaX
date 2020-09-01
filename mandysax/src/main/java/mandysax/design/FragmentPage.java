package mandysax.design;
import android.app.*;
import android.content.*;
import android.util.*;
import android.widget.*;
import java.util.*;

public class FragmentPage extends FrameLayout
{

	private final FragmentTransaction transaction;

	private final List<Fragment> list=new ArrayList<Fragment>();;

	public FragmentPage(Context p0)
	{
		super(p0);
		transaction = ((Activity)p0).getFragmentManager().beginTransaction();
	}

	public FragmentPage(Context p0, AttributeSet p1)
	{
		super(p0, p1);
		transaction = ((Activity)p0).getFragmentManager().beginTransaction();
	}

	public List<Fragment> add(Fragment p0, Fragment p1, Fragment p2, Fragment p3)
	{
		list.add(p0);
		list.add(p1);
		list.add(p2);
		list.add(p3);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1, Fragment p2)
	{
		list.add(p0);
		list.add(p1);
		list.add(p2);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1)
	{
		list.add(p0);
		list.add(p1);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1, Fragment p2, Fragment p3, int index)
	{
		list.add(p0);
		list.add(p1);
		list.add(p2);
		list.add(p3);
		showFragment(index);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1, Fragment p2, int index)
	{
		list.add(p0);
		list.add(p1);
		list.add(p2);
		showFragment(index);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1, int index)
	{
		list.add(p0);
		list.add(p1);
		showFragment(index);
		return list;
	}

	public void showFragment(int index)
	{
		final FragmentTransaction transaction = ((Activity)getContext()).getFragmentManager().beginTransaction();
		for (int i=0;i < list.size();i++)
		{
			if (!list.get(i).isHidden())
			{
				transaction.hide(list.get(i));
			}
		}
		transaction.show(list.get(index));
		transaction.commit();
	}

}

