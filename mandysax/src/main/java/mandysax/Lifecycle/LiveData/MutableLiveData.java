package mandysax.Lifecycle.LiveData;
import android.os.Message;
import java.util.*;
import mandysax.Service.*;

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

