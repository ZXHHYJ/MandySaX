package mandysax.utils.data.repository;

public class Repository<T extends Object>
{

    public void getNetworkData(Key key, DataCallback<T> callBack)
    {
        return;
    }

    public void getLocalData(Key key, DataCallback<T> callBack)
    {
        return;
    }

    protected void putData(T data)
    {
        return;
    }

    protected T getData(String key)
    {
        return null;
    }
    
}
