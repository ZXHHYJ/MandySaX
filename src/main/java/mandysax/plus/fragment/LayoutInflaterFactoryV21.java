package mandysax.plus.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import mandysax.R;

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
        if (name.equalsIgnoreCase("fragment") || name.equalsIgnoreCase("Fragment"))
			if (context instanceof FragmentActivity)
			{
				final FragmentActivity activity=(FragmentActivity) context;
				final String classPath = attrs.getAttributeValue(R.styleable.Fragment_android_name);
				final FrameLayout fragmentParent = new FrameLayout(context, attrs);
				final String tag=fragmentParent.getId() + "";
				if (activity.getFragmentPlusManager().findFragmentByTag(tag) == null)
					try
					{
						final Fragment fragment = (Fragment)Class.forName(classPath).newInstance();
						activity.getFragmentPlusManager().add(0, fragment, tag).commit();
						fragmentParent.post(new Runnable(){
								@Override
								public void run()
								{
									activity.getFragmentPlusManager().add(fragmentParent.getId(), fragment, tag).show(fragment).commit();					
								}
							});
					}
					catch (Exception e)
					{
						throw new RuntimeException(e.getMessage());
					}
				return fragmentParent;
			}
        return null;
    }
}
