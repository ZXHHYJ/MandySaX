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

	private final ArrayList<Fragment> fragmentList=new ArrayList<Fragment>();

	public FragmentPage(Context p0)
	{
		super(p0);
	}

	public FragmentPage(Context p0, AttributeSet p1)
	{
		super(p0, p1);
	}

	public void add(Class... fragmentClass)
	{
		for (Class _fragmentClass:fragmentClass)
		{
            //Log.d(getClass().getCanonicalName(),_fragmentClass.toString());
			Fragment fragment= initFragment(_fragmentClass);
			/*if (fragment instanceof Fragment)
			 {
			 fragment.setFragmentPage(this);
			 }*/
			if (fragment != null)
			{
				fragmentList.add(fragment);
			}
			else throw new NullPointerException("Could not initialize fragment:" + _fragmentClass.getName());
		}
	}

	private Fragment initFragment(Class _fragmentClass)
	{
		try
		{
			Fragment fragment=((FragmentActivity)getContext()).findFragmentByTag(_fragmentClass.getCanonicalName());
			if (fragment == null)fragment = (Fragment) Class.forName(_fragmentClass.getCanonicalName()).newInstance();
			if (!fragment.isAdded())
				((FragmentActivity)getContext()).getMannger().add(getId(), fragment, _fragmentClass.getCanonicalName()).hide(fragment);	
			return fragment;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void showFragment(int index)
	{
        //if (isMain)
        if (index < fragmentList.size())
		{

			FragmentMannger mannger = ((FragmentActivity)getContext()).getMannger();
			for (int i=0;i < fragmentList.size();i++)
			{
				if (!fragmentList.get(i).isHidden())
				{
					mannger.hide(fragmentList.get(i));
				}
			}	
			mannger.show(fragmentList.get(index));
		}
		else throw new  ArrayIndexOutOfBoundsException("index > Fragment List!");
	}
}
