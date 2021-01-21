package mandysax.plus.lifecycle;
import android.util.ArrayMap;

public class Lifecycle
{
	private final ArrayMap<String,LifecycleObserver> event = new ArrayMap<String,LifecycleObserver>();

	public void addObsever(LifecycleObserver... observer)
	{
		for (LifecycleObserver event:observer)
			this.event.put(event.toString(), event);
	}

	private void PostSate(Event State)
	{
		for (LifecycleObserver observer:event.values())
			observer.Observer(State);
	}

	public void onCreate()
	{
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
		event.clear();
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
