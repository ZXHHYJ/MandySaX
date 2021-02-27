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

	private void PostSate(String State)
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

	public static class Event
	{
		public static final String
		ON_CREATE="Lifecycle.Event.ON_CREATE",
		ON_START="Lifecycle.Event.ON_START",
		ON_RESUME="Lifecycle.Event.ON_RESUME",
		ON_PAUSE="Lifecycle.Event.ON_PAUSE",
		ON_STOP="Lifecycle.Event.ON_STOP",
		ON_DESTORY="Lifecycle.Event.ON_DESTORY";
	}

}
