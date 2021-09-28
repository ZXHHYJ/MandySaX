package mandysax.lifecycle;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author liuxiaoliu66
 */
public final class Lifecycle implements LifecycleImpl {

    private final CopyOnWriteArrayList<LifecycleObserver> mObservers = new CopyOnWriteArrayList<>();

    public Event event;

    @Override
    public void addObserver(LifecycleObserver... observer) {
        mObservers.addAll(Arrays.asList(observer));
        postEvent(event);
    }

    @Override
    public void removeObserver(LifecycleObserver... observers) {
        if (!mObservers.isEmpty()) {
            if (observers.length > 1)
                mObservers.removeAll(Arrays.asList(observers));
            else mObservers.remove(observers[0]);
        }
    }

    private void postEvent(Event newEvent) {
        event = newEvent;
        for (LifecycleObserver observer : mObservers) {
            observer.Observer(newEvent);
            /*observer.Observer(Event.ON_ANY);*/
        }
    }

    @Override
    public void dispatchOnCreate() {
        postEvent(Event.ON_CREATE);
    }

    @Override
    public void dispatchOnStart() {
        postEvent(Event.ON_START);
    }

    @Override
    public void dispatchOnRestart() {
        postEvent(Event.ON_RESTART);
    }

    @Override
    public void dispatchOnResume() {
        postEvent(Event.ON_RESUME);
    }

    @Override
    public void dispatchOnPause() {
        postEvent(Event.ON_PAUSE);
    }

    @Override
    public void dispatchOnStop() {
        postEvent(Event.ON_STOP);
    }

    @Override
    public void dispatchOnDestroy() {
        postEvent(Event.ON_DESTROY);
        mObservers.clear();
    }

    public enum Event {

        //记录当前状态，size对比
        ON_CREATE(0),

        ON_START(1),

        ON_RESUME(2),

        ON_RESTART(3),

        ON_PAUSE(4),

        ON_STOP(5),

        ON_DESTROY(6);

        //废弃
        /*ON_ANY(-1);*/

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
