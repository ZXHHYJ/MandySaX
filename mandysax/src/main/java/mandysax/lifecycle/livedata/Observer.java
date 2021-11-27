package mandysax.lifecycle.livedata;

/**
 * @author liuxiaoliu66
 */
public interface Observer<T> {
    /**
     * value changed
     * @param p1 new Value
     */
    void onChanged(T p1);
}
