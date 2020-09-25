package mandysax.data;
import java.util.HashMap;

public class SafetyHashMap<K extends java.lang.Object, V extends java.lang.Object> extends HashMap<K,V>
{
	private static final String BUG_MESSAGR="Cannot submit empty data!";

	@Override
	public V replace(K key, V value)
	{
		if (value == null)throw new NullPointerException(BUG_MESSAGR);
		return super.replace(key, value);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue)
	{
		if (newValue == null)throw new NullPointerException(BUG_MESSAGR);
		return super.replace(key, oldValue, newValue);
	}
	

	@Override
	public V put(K key, V value)
	{
		if (value == null)throw new NullPointerException(BUG_MESSAGR);
		return super.put(key, value);
	}

}
