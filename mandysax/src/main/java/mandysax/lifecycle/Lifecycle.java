package mandysax.lifecycle;
import java.util.*;

public class Lifecycle extends Object
{
	private final List<LifecycleObserver> event = new ArrayList<LifecycleObserver>();
	
	public void addObsever(LifecycleObserver... observer){
		for(LifecycleObserver event:observer){
			this.event.add(event);
		}
	}
	
	private void PostSate(int State){
		for(LifecycleObserver observer:event)
		observer.Observer(State);
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
	
	void onKill(){
		PostSate(0);
	}

	public static class Event{
		public static int ON_START=1;
		public static int ON_RESUME=2;
		public static int ON_PAUSE=3;
		public static int ON_STOP=4;
		public static int ON_DESTORY=5;
	}
	
}
