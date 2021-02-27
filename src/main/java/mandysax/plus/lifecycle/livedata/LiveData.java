package mandysax.plus.lifecycle.livedata;
import android.os.Handler;
import android.os.Message;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import mandysax.plus.lifecycle.Lifecycle;
import mandysax.plus.lifecycle.LifecycleObserver;
import mandysax.plus.lifecycle.LifecycleOwner;
import mandysax.plus.lifecycle.livedata.Observer;

public abstract class LiveData <T extends Object>
{

	protected volatile T content;

	protected final List<Observer<? super T>> callBack = new CopyOnWriteArrayList<Observer<? super T>>();

	protected final LiveDataCallBack cb = new LiveDataCallBack();

	public T getValue()
	{
		return content;
	}

    public LiveData<T> apply(final Observer<? super T> p1)
    {
        if (p1 == null)return this; 
        if (getValue() == null)
            observeForever(new Observer<T>(){
                    @Override
                    public void onChanged(T p2)
                    {
						if (content != null)
						{
							p1.onChanged(content);
							callBack.remove(this);
						}
                    }
                });
        else p1.onChanged(getValue());
        return this;
    }

	public LiveData<T> observeForever(Observer<? super T> p1)
	{
		callBack.add(p1);
		return this;
	}

	public LiveData<T> observe(LifecycleOwner p0, final Observer<? super T> p1)
	{
		p0.getLifecycle().addObsever(new LifecycleObserver(){

				@Override
				public void Observer(String State)
				{
					if (State == Lifecycle.Event.ON_DESTORY)
						callBack.remove(p1);
				}

			});
		callBack.add(p1);
		return this;
	}

    protected void start()
	{
		for (Observer<? super T> c:callBack)
			c.onChanged(content);
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

