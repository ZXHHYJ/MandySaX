package mandysax.lifecycle.livedata;

import androidx.annotation.NonNull;

import java.util.concurrent.CopyOnWriteArrayList;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;

/**
 * @param <T> 需要包装的泛型类型
 */
public class LiveData<T> {

    /**
     * 用于存储lifecycle和与其对应的observer
     */
    class Event {
        public final Lifecycle lifecycle;
        public final Observer<? super T> observer;

        public Event(Lifecycle lifecycle, Observer<? super T> observer) {
            this.lifecycle = lifecycle;
            this.observer = observer;
            if (lifecycle != null) {
                lifecycle.addObserver(state -> {
                    if (state.exceedTo(Lifecycle.Event.ON_STOP)) {
                        removeObserver(observer);
                    }
                });
            }
        }

        /**
         * 判断是否需要对observer通知value的更新
         */
        public void start() {
            if (lifecycle == null) {
                observer.onChanged(content);
                return;
            }
            if (!lifecycle.event.exceedTo(Lifecycle.Event.ON_STOP)) {
                observer.onChanged(content);
            }
        }
    }

    protected T content;

    protected final CopyOnWriteArrayList<Event> events = new CopyOnWriteArrayList<>();

    /**
     * @return livedata托管的value
     */
    public T getValue() {
        return content;
    }

    /**
     * @param lifecycleOwner 生命周期所有者
     * @param observer       观察者
     */
    public void lazy(LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
        if (content != null) {
            observer.onChanged(content);
            return;
        }
        observe(lifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(T p1) {
                if (content != null) {
                    observer.onChanged(content);
                    removeObserver(this);
                }
            }
        });
    }

    /**
     * @param observer 观察者
     */
    public void lazy(Observer<? super T> observer) {
        if (content != null) {
            observer.onChanged(content);
            return;
        }
        observeForever(new Observer<T>() {
            @Override
            public void onChanged(T p2) {
                if (content != null) {
                    observer.onChanged(content);
                    removeObserver(this);
                }
            }
        });
    }

    /**
     * @param observer 观察者
     */
    public void observeForever(Observer<? super T> observer) {
        events.add(new Event(null, observer));
        if (content != null) observer.onChanged(content);
    }

    /**
     * @param lifecycleOwner 生命周期所有者
     * @param observer       观察者
     */
    public void observe(@NonNull LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
        events.add(new Event(lifecycleOwner.getLifecycle(), observer));
        if (content != null) observer.onChanged(content);
    }

    /**
     * @param observer 需要移除的观察者
     */
    public void removeObserver(Observer<? super T> observer) {
        for (Event event : events) {
            if (event.observer.equals(observer)) {
                events.remove(event);
            }
        }
    }

    /**
     * 通知所有event，value已更新
     */
    protected void start() {
        for (Event event : events) {
            event.start();
        }
    }

}

