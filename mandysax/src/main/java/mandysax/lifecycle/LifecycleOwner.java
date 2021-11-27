package mandysax.lifecycle;

/**
 * @author ZXHHYJ
 */
public interface LifecycleOwner {
    /**
     * 获取托管的lifecycle
     *
     * @return lifecycle
     */
    Lifecycle getLifecycle();
}
