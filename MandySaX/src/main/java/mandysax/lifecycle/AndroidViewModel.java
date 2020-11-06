package mandysax.lifecycle;
import android.content.Context;

class AndroidViewModel
{
	private static Context mApplication;

    static void init(Context context)
    {
        if (mApplication == null)
            mApplication = context;
	}
    
	public Context getApplication()
	{
		return mApplication;
	}

}
