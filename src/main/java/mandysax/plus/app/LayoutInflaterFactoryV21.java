package mandysax.plus.app;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import mandysax.R;
import mandysax.design.fragmentpage.widget.FragmentPage;
import mandysax.design.navigationbar.widget.BottomNavigationBar;
import mandysax.plus.fragment.FragmentActivity;

public class LayoutInflaterFactoryV21 implements LayoutInflater.Factory2
{

	@Override
	public View onCreateView(String p1, Context p2, AttributeSet p3)
	{
		return null;
	}

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        if (name.equalsIgnoreCase("BottomNavigationBar")) 
            return new BottomNavigationBar(context, attrs);
        if (name.equalsIgnoreCase("FragmentPage"))
            return new FragmentPage(context, attrs);
        if (name.equalsIgnoreCase("fragment") || name.equalsIgnoreCase("Fragment"))
			if (context instanceof FragmentActivity)
			{
				final FragmentActivity activity=(FragmentActivity) context;
				final String classPath = attrs.getAttributeValue(R.styleable.Fragment_android_name);
				final FrameLayout fragmentParent = new FrameLayout(context, attrs);
				if (fragmentParent.getId() == -1)
					throw new RuntimeException("Fragment " + classPath + " layout ID cannot be empty");
				if (activity.getFragmentPlusManager().findFragmentByTag(fragmentParent.getId() + "") == null)
				{
					fragmentParent.post(new Runnable(){
							@Override
							public void run()
							{
								try
								{
									activity.getFragmentPlusManager().replace(fragmentParent.getId(), Class.forName(classPath), fragmentParent.getId() + "").commit();
								}
								catch (ClassNotFoundException e)
								{
									throw new RuntimeException(e.getMessage());
								}
							}
						});
				}
				return fragmentParent;
			}
        return null;
    }
}
