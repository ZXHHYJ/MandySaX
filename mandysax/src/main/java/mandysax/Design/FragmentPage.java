package mandysax.Design;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;

public class FragmentPage extends FrameLayout
{

	private FragmentTransaction transaction;

	private List<Fragment> list;
	
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

	public List<Fragment> add(List<Fragment> list, int page)
	{
		addlist(list, page);
		return list;
	}

	public List<Fragment> add(List<Fragment> list)
	{
		addlist(list, 0);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1, Fragment p2, Fragment p3)
	{
		List<Fragment> list= new ArrayList<Fragment>();
		list.add(p0);
		list.add(p1);
		list.add(p2);
		list.add(p3);
		addlist(list, 0);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1, Fragment p2)
	{
		List<Fragment> list= new ArrayList<Fragment>();
		list.add(p0);
		list.add(p1);
		list.add(p2);
		addlist(list, 0);
		return list;
	}

	public List<Fragment> add(Fragment p0, Fragment p1)
	{
		List<Fragment> list= new ArrayList<Fragment>();
		list.add(p0);
		list.add(p1);
		addlist(list, 0);
		return list;
	}

	private void addlist(List<Fragment> list, int page)
	{
		this.list=list;
		for (int i = 0;i < list.size();i++)	
		{	
            if (!list.get(i).isAdded())
                transaction.add(getId(), list.get(i), i + "").hide(list.get(i));
		}
		transaction.show(list.get(page));
		transaction.commit();
	}
	
	public void showFragment( int p2)
	{
		final FragmentTransaction transaction = ((Activity)getContext()).getFragmentManager().beginTransaction();
		for (int i=0;i < list.size();i++)
		{
			if (!list.get(i).isHidden())
			{
				transaction.hide(list.get(i));
			}
		}
		transaction.show(list.get(p2));
		transaction.commit();
	}

}
