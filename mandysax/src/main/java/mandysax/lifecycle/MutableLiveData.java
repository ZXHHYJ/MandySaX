package mandysax.lifecycle;
import android.os.Message;

public class MutableLiveData <T extends Object> extends LiveData<T>
{

	public MutableLiveData(T p1)
	{content = p1;}

	public MutableLiveData()
	{}

	public void postValue(final T p1)
	{	
		content = p1;
		final Message message = new Message();
		message.obj = this;				
		cb.sendMessage(message);	
	}

    public void setValue(T p1)
	{
		content = p1;
		start();
	}

}

