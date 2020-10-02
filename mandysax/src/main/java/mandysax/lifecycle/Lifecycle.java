package mandysax.lifecycle;
import java.util.ArrayList;
import java.util.List;
import mandysax.data.SafetyHashMap;

enum DataEnum
{
	LIFECYCLE(new SafetyHashMap<String ,Lifecycle>());
	private final SafetyHashMap<String,Object> con;

	private DataEnum(final SafetyHashMap map){
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
public class Lifecycle
{
	private final List<LifecycleObserver> event = new ArrayList<LifecycleObserver>();

	public void addObsever(LifecycleObserver... observer)
	{
		for (LifecycleObserver event:observer)
			this.event.add(event);
	}
	
	private void PostSate(Event State)
	{
		for (LifecycleObserver observer:event)
			observer.Observer(State);
	}

	protected void onCreate()
	{
		PostSate(Event.ON_CREATE);
	}

	protected void onStart()
	{
		PostSate(Event.ON_START);
	}

	protected void onResume()
	{
		PostSate(Event.ON_RESUME);
	}

	protected void onPause()
	{
		PostSate(Event.ON_PAUSE);
	}

	protected void onStop()
	{
		PostSate(Event.ON_STOP);
	}

	protected void onDestory()
	{
		PostSate(Event.ON_DESTORY);
	}

	public enum Event
	{
		ON_CREATE,
		ON_START,
		ON_RESUME,
		ON_PAUSE,
		ON_STOP,
		ON_DESTORY,
	}

}
