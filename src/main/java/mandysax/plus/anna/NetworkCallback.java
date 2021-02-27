package mandysax.plus.anna;

public interface NetworkCallback<T>
{
	public void onLoading(boolean isLoaded,T model);
    public void onError(String msg);
}
