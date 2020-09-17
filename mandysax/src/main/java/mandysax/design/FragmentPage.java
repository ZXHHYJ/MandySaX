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

	public List<Fragment> add(Fragment... list)
	{
		for(Fragment fragment:list){
			this.list.add(fragment);
		}
		return this.list;
	}
	
	public List<Fragment> add(Fragment... list,int index)
	{
		for(Fragment fragment:list){
			this.list.add(fragment);
		}
		showFragment(index);
		return this.list;
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

