package mandysax.plus.lifecycle;
import android.content.Context;

public class AndroidViewModel extends ViewModel
{
	private static Context mApplication;

    static void init(Context context)
    {
            mApplication = context;
	}
    
	public Context getApplication()
	{
		return mApplication;
	}

}
