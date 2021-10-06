package mandysax.lifecycle;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public final class LifecycleRegistry extends Lifecycle {

    private final ArrayList<LifecycleObserver> mObservers = new ArrayList<>();

    @Override
    public void addObserver(@NonNull LifecycleObserver... observers) {
        for (LifecycleObserver observer : observers) {
            mObservers.add(observer);
            if (event != null)
                observer.Observer(event);
        }
    }

    @Override
    public void removeObserver(@NonNull LifecycleObserver... observers) {
        for (LifecycleObserver observer : observers) {
            mObservers.remove(observer);
        }
    }

    public void markState(Lifecycle.Event state) {
        event = state;
        int size = mObservers.size();
        for (int i = 0; i < size; i++)
            if (i < mObservers.size())
                mObservers.get(i).Observer(state);
        if (state == Event.ON_DESTROY) {
            mObservers.clear();
        }
    }

}

