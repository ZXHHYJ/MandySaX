package mandysax.lifecycle.livedata;

public abstract interface switchMap<T extends LiveData,V extends Object>
{
	abstract LiveData<V> map(T p1)
}
