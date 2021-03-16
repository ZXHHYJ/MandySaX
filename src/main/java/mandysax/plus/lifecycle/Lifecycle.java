package mandysax.plus.lifecycle;
import java.util.concurrent.ConcurrentHashMap;

public class Lifecycle implements LifecycleImpl
{
	/*
	 *ConcurrentHashMap can fix bug java.lang.ArrayIndexOutOfBoundsException fo ArrayMap
	 */
	private final ConcurrentHashMap<String,LifecycleObserver> event = new ConcurrentHashMap<String,LifecycleObserver>();

    @Override
	public void addObsever(LifecycleObserver... observer)
	{
		for (LifecycleObserver event:observer)
		{
			if (this.event.get(event.getClass() + "") == null)
				this.event.put(event.getClass() + "", event);
		}
	}

	private void PostSate(String State)
	{
		for (LifecycleObserver observer:event.values())
			observer.Observer(State);
	}

    @Override
	public void onCreate()
	{
		PostSate(Event.ON_CREATE);
	}

    @Override
	public void onStart()
	{
		PostSate(Event.ON_START);
	}

    @Override
    public void onRestart()
    {
        PostSate(Event.ON_RESTART);
    }

    @Override
	public void onResume()
	{
		PostSate(Event.ON_RESUME);
	}

    @Override
	public void onPause()
	{
		PostSate(Event.ON_PAUSE);
	}

    @Override
	public void onStop()
	{
		PostSate(Event.ON_STOP);
	}

    @Override
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
        ON_RESTART="Lifecycle.Event.ON_RESUME",
		ON_RESUME="Lifecycle.Event.ON_RESUME",
		ON_PAUSE="Lifecycle.Event.ON_PAUSE",
		ON_STOP="Lifecycle.Event.ON_STOP",
		ON_DESTORY="Lifecycle.Event.ON_DESTORY";
	}

}
