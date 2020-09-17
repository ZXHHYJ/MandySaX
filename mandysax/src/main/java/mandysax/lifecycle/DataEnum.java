package mandysax.lifecycle;
import java.util.*;

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
