package mandysax.plus.lifecycle.livedata;

public interface switchMap<T extends LiveData,V extends Object>
{
	public LiveData<V> map(T p1)
}
