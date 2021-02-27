package mandysax.plus.repository;
import java.util.LinkedHashMap;
import java.util.Set;

public final class Key
{
    private final LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();

    public Key putString(String key, String string)
    {
        map.put(key, string);
        return this;
    }

    public String getString(String key)
    {
        return (String)map.get(key);
    }

    public Key putInt(String key, int i)
    {
        map.put(key, i);
        return this;
    }

    public int getInt(String key)
    {
        return (int)map.get(key);
    }

    public Key putBoolean(String key, boolean Boolean)
    {
        map.put(key, Boolean);
        return this;
    }

    public boolean getBoolean(String key)
    {
        return (boolean)map.get(key);
    }

	public Object getObject(String key)
	{
		return map.get(key);
	}

	public Set<String> getKetSet()
	{
		return map.keySet();
	}

	public static Key getKey()
	{
		return new Key();
	}

} 
