package mandysax.lifecycle;

public abstract interface Observer<T>
{
	abstract void onChanged(T p1);
}
