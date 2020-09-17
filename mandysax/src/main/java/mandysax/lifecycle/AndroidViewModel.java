package mandysax.lifecycle;
import android.content.Context;
import java.util.TreeMap;

final enum DataEnum
{
	VIEWMODEL(new TreeMap<String ,ViewModel>()),
	LIFECYCLE(new TreeMap<String ,Lifecycle>());
	private final TreeMap<String,Object> con;

	private DataEnum(final TreeMap map){
		con=map;
	}

	public void put(String key,Object value){
		con.put(key,value);
	}

	public Object get(String key){
		return con.get(key);
	}

	public void remove(String key){
		con.remove(key);
	}

}
class AndroidViewModel
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
