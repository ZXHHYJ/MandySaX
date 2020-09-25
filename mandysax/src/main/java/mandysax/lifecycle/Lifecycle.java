package mandysax.lifecycle;
import java.util.ArrayList;
import java.util.List;
import mandysax.data.SafetyHashMap;

final enum DataEnum
{
	VIEWMODEL(new SafetyHashMap<String ,ViewModel>()),
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
	
	public void onCreate(){
		PostSate(Event.ON_CREATE);
	}

	public void onStart()
	{
		PostSate(Event.ON_START);
	}

	public void onResume()
	{
		PostSate(Event.ON_RESUME);
	}

	public void onPause()
	{
		PostSate(Event.ON_PAUSE);
	}

	public void onStop()
	{
		PostSate(Event.ON_STOP);
	}

	public void onDestory()
	{
		PostSate(Event.ON_DESTORY);
	}

	protected void onKill()
	{
		PostSate(Event.ON_KILL);
	}

	public final enum Event
	{
		ON_CREATE,
		ON_START,
		ON_RESUME,
		ON_PAUSE,
		ON_STOP,
		ON_DESTORY,
		ON_KILL;
	}

}
