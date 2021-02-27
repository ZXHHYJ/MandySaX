package mandysax.plus.app;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import mandysax.design.fragmentpage.widget.FragmentPage;
import mandysax.design.navigationbar.widget.BottomNavigationBar;

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

        if (name.equals("BottomNavigationBar")) 
            return new BottomNavigationBar(context, attrs);
        if (name.equalsIgnoreCase("FragmentPage"))
            return new FragmentPage(context, attrs);
        if (name.equals("fragment")) 
        {
            System.out.println(attrs.getAttributeName(android.R.attr.name));
        }
        return null;
    }
}
