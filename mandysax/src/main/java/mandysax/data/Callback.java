package mandysax.data;

public interface Callback<T>
{
	public void onStart(T decodeStream);
	public void onEnd(boolean bug);
}
