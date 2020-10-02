package mandysax.lifecycle;
import android.content.Context;

class AndroidViewModel
{
	private static Context mApplication;

	protected Context getApplication()
	{
		return mApplication;
	}

	protected static void init(Context context)
	{
		if (mApplication == null)
			mApplication = context;
	}

}
