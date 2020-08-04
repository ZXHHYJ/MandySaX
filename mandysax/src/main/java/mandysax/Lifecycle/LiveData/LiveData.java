package mandysax.Lifecycle.LiveData;
import android.os.Handler;
import android.os.Message;
import java.util.ArrayList;
import java.util.List;
import mandysax.Lifecycle.LifecycleAbstract;
import mandysax.Lifecycle.LifecycleEvent.LifecycleDestory;

public abstract class LiveData <T extends Object>
{

	protected T content;

	protected List<Observer<? super T>> Callback = new ArrayList<Observer<? super T>>();

	protected final LiveDataCallBack cb = new LiveDataCallBack();

	public void observeForever(Observer<? super T> p1)
	{
		Callback.add(p1);
	}

	public void observe(final LifecycleAbstract p0,final Observer<? super T> p1)
	{
		p0.getLifecycle().DestoryEvent(new LifecycleDestory(){
				@Override
				public void onDestory()
				{
					Callback.remove(p1);
				}
			});
		Callback.add(p1);
	}
	
	public T getValue(){
		return content;
	}
	
    void start()
	{
		for (int i =0;i < Callback.size();i++)
			Callback.get(i).onChanged(content);
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

