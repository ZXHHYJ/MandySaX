package mandysax.plus.app;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import mandysax.plus.fragment.Fragment;
import mandysax.plus.fragment.FragmentActivity;

public class ActivityPlus extends FragmentActivity implements ActivityPlusImpl
{

	public static final String FRAGMENT_TAG=WXY + "";

	@Override
	public Fragment getActivityFragment()
	{
		return getFragmentPlusManager().findFragmentByTag(FRAGMENT_TAG);	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getLayoutInflater().setFactory2(new LayoutInflaterFactoryV21());
	}

	@Override
	public void setContentView(Class fragmentClass)
	{
		FrameLayout layout=new FrameLayout(this);
		layout.setId(WXY);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    setContentView(layout);
		if (getActivityFragment() == null)
		{
			getFragmentPlusManager().replace(WXY, fragmentClass, FRAGMENT_TAG).commit();
		}
	}

}
