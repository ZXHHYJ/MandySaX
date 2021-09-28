package mandysax.lifecycle;

public interface LifecycleControllerImpl {

    void dispatchOnCreate();

    void dispatchOnStart();

    void dispatchOnRestart();

    void dispatchOnResume();

    void dispatchOnPause();

    void dispatchOnStop();

    void dispatchOnDestroy();
}
