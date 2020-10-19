package mandysax.lifecycle.livedata;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.List;
import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;
import mandysax.lifecycle.LifecycleOwner;

public abstract class LiveData <T extends Object>
{

	protected T content;

	protected final List<Observer<? super T>> callBack = new ArrayList<Observer<? super T>>();

	private final List<Observer<? super T>> task_callBack = new ArrayList<Observer<? super T>>();

	protected final LiveDataCallBack cb = new LiveDataCallBack();

	private Lifecycle lifecycle;

	private boolean active=true;

	public void observeForever(Observer<? super T> p1)
	{
		callBack.add(p1);
	}

	public void observe(final LifecycleOwner p0, final Observer<? super T> p1)
	{
		lifecycle = p0.getLifecycle();
		lifecycle.addObsever(new LifecycleObserver(){

				@Override
				public void Observer(Lifecycle.Event State)
				{
					if (State == Lifecycle.Event.ON_DESTORY)
					{
						active = false;
						callBack.remove(p1);
					}
					else
					if (State == Lifecycle.Event.ON_PAUSE || State == Lifecycle.Event.ON_STOP || State == Lifecycle.Event.ON_DESTORY)
					{
						active = false;
					}
					else
					{
						active = true;
						for (Observer<? super T> c:task_callBack)
							c.onChanged(content);
						task_callBack.clear();
					}
				}

			});
		callBack.add(p1);
	}

	public T getValue()
	{
		return content;
	}

    protected void start()
	{
		for (Observer<? super T> c:callBack)
			if (active)
				c.onChanged(content);
			else
			{
				task_callBack.add(c);
			}
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

