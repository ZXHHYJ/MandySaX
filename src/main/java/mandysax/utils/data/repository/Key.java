package mandysax.utils.data.repository;
import java.io.Serializable;
import android.util.ArrayMap;

public final class Key implements Serializable
{
    private final ArrayMap map = new ArrayMap();

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

} 
