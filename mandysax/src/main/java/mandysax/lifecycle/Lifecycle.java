package mandysax.lifecycle;

/**
 * @author liuxiaoliu66
 */
public abstract class Lifecycle {

    public Lifecycle.Event event;

    public abstract void addObserver(LifecycleObserver observer);

    public abstract void removeObserver(LifecycleObserver observer);

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
