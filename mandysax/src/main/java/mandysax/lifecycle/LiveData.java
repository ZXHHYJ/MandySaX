package mandysax.lifecycle;
import android.os.*;
import java.util.*;

public abstract class LiveData <T extends Object>
{

	protected T content;

	protected List<Observer<? super T>> callBack = new ArrayList<Observer<? super T>>();

	protected final LiveDataCallBack cb = new LiveDataCallBack();

	public void observeForever(Observer<? super T> p1)
	{
		callBack.add(p1);
	}

	public void observe(final LifecycleOwner p0,final Observer<? super T> p1)
	{
		
		p0.getLifecycle().addObsever(new LifecycleObserver(){

				@Override
				public void Observer(int State)
				{
					callBack.remove(p1);
				}
				
		});
		callBack.add(p1);
	}
	
	public T getValue(){
		return content;
	}
	
    protected void start()
	{
		for (Observer<? super T> c:callBack)
			c.onChanged(content);
	}

	private class LiveDataCallBack extends Handler 
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			((LiveData)msg.obj).start();
		}
	}
}

