package mandysax.lifecycle;
import android.util.ArrayMap;

public class Lifecycle
{
	private final ArrayMap<String,LifecycleObserver> event = new ArrayMap<String,LifecycleObserver>();

	public void addObsever(LifecycleObserver... observer)
	{
		for (LifecycleObserver event:observer)
			this.event.put(event.toString(),event);
	}

	private void PostSate(Event State)
	{
		for (LifecycleObserver observer:event.values())
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
