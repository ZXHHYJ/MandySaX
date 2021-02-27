package mandysax.plus.repository;
import mandysax.plus.anna.Anna;
import mandysax.plus.lifecycle.AndroidViewModel;

public class Repository<T extends Object,A extends Anna> extends AndroidViewModel
{

	private final A mAnna;
	
	public Repository(A anna){
		mAnna=anna;
	}
	
    protected void getNetworkData(Key key, DataCallback<T> callBack)
    {
    }

    protected void getLocalData(Key key, DataCallback<T> callBack)
    {
    }
	
	public A getAnna(){
		return mAnna;
	}

	@Override
	protected final void onCleared()
	{
		super.onCleared();
	}
	
}
