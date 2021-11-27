package mandysax.lifecycle;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * @author ZXHHYJ
 */
public final class LifecycleRegistry extends Lifecycle {

    private final ArrayList<LifecycleObserver> mObservers = new ArrayList<>();

    @Override
    public void addObserver(@NonNull LifecycleObserver observer) {
        mObservers.add(observer);
        if (event != null) {
            observer.observer(event);
        }
    }

    @Override
    public void removeObserver(@NonNull LifecycleObserver observer) {
        mObservers.remove(observer);
    }

    public void markState(Lifecycle.Event state) {
        event = state;
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).observer(state);
        }
        if (state == Event.ON_DESTROY) {
            mObservers.clear();
        }
    }

}

