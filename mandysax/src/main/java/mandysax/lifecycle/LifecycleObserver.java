package mandysax.lifecycle;

/**
 * @author ZXHHYJ
 */
public interface LifecycleObserver {
    /**
     * state changed
     * @param state The Lifecycle.Event corresponding to the lifecycle event
     */
    void observer(Lifecycle.Event state);
}
