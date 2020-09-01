package mandysax.lifecycle;
import android.content.*;

class AndroidViewModel extends Object
{
	private static Context application;
	
	{
		DataEnum.VIEWMODEL.put(getClass().getCanonicalName(), this);
	}
	
	public Context getApplication(){
		return application;
	}
	
	public static void init(Context con){
		application=con;
	}
	
	public void onCleared(){
		DataEnum.VIEWMODEL.remove(getClass().getCanonicalName());
	}
	
}
