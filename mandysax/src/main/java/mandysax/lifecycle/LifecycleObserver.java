package mandysax.lifecycle;

public interface LifecycleObserver {
    /**
     * @param state The Lifecycle.Event corresponding to the lifecycle event
     */
    void Observer(Lifecycle.Event state);
}
