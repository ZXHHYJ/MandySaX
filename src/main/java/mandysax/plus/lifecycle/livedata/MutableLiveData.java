package mandysax.plus.lifecycle.livedata;
import android.os.Message;
import mandysax.plus.lifecycle.LifecycleOwner;

public class MutableLiveData <T extends Object> extends LiveData<T>
{

	public MutableLiveData(T p1)
	{
		content = p1;
	}

	public MutableLiveData()
	{
    }

    @Override
    public MutableLiveData<T> apply(Observer<? super T> p1)
    {
        return(MutableLiveData<T>)super.apply(p1);
    }

	@Override
	public MutableLiveData<T> observeForever(Observer<? super T> p1)
	{
		return(MutableLiveData<T>)super.observeForever(p1);
	}

	@Override
	public MutableLiveData<T> observe(LifecycleOwner p0, Observer<? super T> p1)
	{
		return(MutableLiveData<T>)super.observe(p0, p1);
	}

	public T postValue(T p1)
	{	
		content = p1;
		final Message message = new Message();
		message.obj = this;				
		cb.sendMessage(message);	
        return p1;
	}

    public T setValue(T p1)
	{
		content = p1;
		start();
        return p1;
	}

}

