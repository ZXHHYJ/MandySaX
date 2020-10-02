package mandysax.lifecycle;
import android.os.*;
import java.util.*;
import mandysax.lifecycle.Lifecycle.Event;

public abstract class LiveData <T extends Object>
{

	protected T content;

	protected final List<Observer<? super T>> callBack = new ArrayList<Observer<? super T>>();

	protected final LiveDataCallBack cb = new LiveDataCallBack();

	private Lifecycle lifecycle;
	
	private boolean active=true;
	
	public void observeForever(Observer<? super T> p1)
	{
		callBack.add(p1);
	}

	public void observe(final LifecycleOwner p0,final Observer<? super T> p1)
	{
		lifecycle=p0.getLifecycle();
		lifecycle.addObsever(new LifecycleObserver(){

				@Override
				public void Observer(Lifecycle.Event State)
				{
					if(State==Lifecycle.Event.ON_DESTORY)
					{
						active=false;
						callBack.remove(p1);
					}else
					if(State==Lifecycle.Event.ON_PAUSE||State==Lifecycle.Event.ON_STOP||State==Lifecycle.Event.ON_DESTORY){
						active=false;
					}else active=true;
				}
				
		});
		callBack.add(p1);
	}
	
	public T getValue(){
		return content;
	}
	
    protected void start()
	{
		if(active)
		for (Observer<? super T> c:callBack)
			c.onChanged(content);
		else lifecycle.addObsever(new LifecycleObserver(){

					@Override
					public void Observer(Lifecycle.Event State)
					{
						if(State==Lifecycle.Event.ON_RESUME){
							start();
						}
					}

				});
	}

	protected class LiveDataCallBack extends Handler 
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			((LiveData)msg.obj).start();
		}
	}
	
}

