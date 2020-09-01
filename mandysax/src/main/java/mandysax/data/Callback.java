package mandysax.data;

public interface Callback<T>
{
	public void onSuccess(T decodeStream);
	public void onFailure()
	public void onEnd();
}
