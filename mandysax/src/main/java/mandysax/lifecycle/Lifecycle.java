package mandysax.lifecycle;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Lifecycle implements LifecycleImpl {

    private CopyOnWriteArrayList<LifecycleObserver> mStack;

    @Override
    public void addObserver(LifecycleObserver... observer) {
        removeObserver(observer);
        mStack.addAll(Arrays.asList(observer));
    }

    @Override
    public void removeObserver(LifecycleObserver... observers) {
        if (mStack == null) mStack = new CopyOnWriteArrayList<>();
        mStack.removeAll(Arrays.asList(observers));
    }

    private void postSate(Event state) {
        if (mStack != null)
            for (LifecycleObserver observer : mStack) {
                observer.Observer(state);
                observer.Observer(Event.ON_ANY);//fix bug form 2.2.0
            }
    }

    @Override
    public void dispatchOnCreate() {
        postSate(Event.ON_CREATE);
    }

    @Override
    public void dispatchOnStart() {
        postSate(Event.ON_START);
    }

    @Override
    public void dispatchOnRestart() {
        postSate(Event.ON_RESTART);
    }

    @Override
    public void dispatchOnResume() {
        postSate(Event.ON_RESUME);
    }

    @Override
    public void dispatchOnPause() {
        postSate(Event.ON_PAUSE);
    }

    @Override
    public void dispatchOnStop() {
        postSate(Event.ON_STOP);
    }

    @Override
    public void dispatchOnDestroy() {
        postSate(Event.ON_DESTROY);
        if (mStack != null)
            mStack.clear();
    }

    public enum Event {

        ON_CREATE(0),

        ON_START(1),

        ON_RESUME(2),

        ON_RESTART(3),

        ON_PAUSE(4),

        ON_STOP(5),

        ON_DESTROY(6),

        ON_ANY(-1);

        private final int mSize;

        Event(int size) {
            mSize = size;
        }

        /**
         * @param event is Lifecycle.Event
         * @return Is the current lifecycle greater than @param event
         */
        public boolean exceedTo(Event event) {
            return mSize > event.mSize;
        }

    }
}
