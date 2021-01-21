package mandysax.utils.data.anna;

public interface NetworkCallback<T>
{
	public void onLoaded(boolean loaded,T loadedClass);
    public void onNetworkError();
}
