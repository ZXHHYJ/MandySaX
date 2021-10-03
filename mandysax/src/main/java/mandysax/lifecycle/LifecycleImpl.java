package mandysax.lifecycle;

public interface LifecycleImpl extends LifecycleControllerImpl {

    void addObserver(LifecycleObserver... observer);

    void removeObserver(LifecycleObserver... observers);

}
