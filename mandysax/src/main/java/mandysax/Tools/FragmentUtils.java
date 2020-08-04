package mandysax.Tools;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import java.util.List;

public class FragmentUtils
{
	public static FragmentTransaction showFragment(Activity p0, List<Fragment> p1, int p2)
	{
		final FragmentTransaction transaction = p0.getFragmentManager().beginTransaction();
		for (int i=0;i < p1.size();i++)
		{
			if (!p1.get(i).isHidden())
			{
				transaction.hide(p1.get(i));
			}
		}
		transaction.show(p1.get(p2));
		return transaction;
	}
}
