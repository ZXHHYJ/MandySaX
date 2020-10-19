package mandysax.lifecycle.livedata;

public abstract interface Observer<T>
{
	abstract void onChanged(T p1);
}
