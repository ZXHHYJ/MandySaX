package mandysax.lifecycle.livedata;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;

/**
 * @param <T> 需要包装的泛型类型
 */
public class LiveData<T> {

    protected T mValue;

    protected final HashMap<Observer<? super T>, Lifecycle> mStore = new HashMap<>();

    /**
     * @return LIveData托管的value
     */
    public T getValue() {
        return mValue;
    }

    /**
     * @param lifecycleOwner 生命周期所有者
     * @param observer       观察者
     */
    public void lazy(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<? super T> observer) {
        if (lifecycleOwner.getLifecycle().event == Lifecycle.Event.ON_DESTROY) {
            // ignore
            return;
        }
        if (mValue != null) {
            observer.onChanged(mValue);
            return;
        }
        observe(lifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(T p1) {
                if (mValue != null) {
                    observer.onChanged(mValue);
                    removeObserver(this);
                }
            }
        });
    }

    /**
     * @param observer 观察者
     */
    public void lazy(Observer<? super T> observer) {
        if (mValue != null) {
            observer.onChanged(mValue);
            return;
        }
        observeForever(new Observer<T>() {
            @Override
            public void onChanged(T p2) {
                if (mValue != null) {
                    observer.onChanged(mValue);
                    removeObserver(this);
                }
            }
        });
    }

    /**
     * @param observer 观察者
     */
    public void observeForever(Observer<? super T> observer) {
        mStore.put(observer, null);
        if (mValue != null) observer.onChanged(mValue);
    }

    /**
     * @param lifecycleOwner 生命周期所有者
     * @param observer       观察者
     */
    public void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Observer<? super T> observer) {
        if (lifecycleOwner.getLifecycle().event == Lifecycle.Event.ON_DESTROY) {
            // ignore
            return;
        }
        mStore.put(observer, lifecycleOwner.getLifecycle());
        lifecycleOwner.getLifecycle().addObserver(state -> {
            if (state.exceedTo(Lifecycle.Event.ON_STOP)) {
                removeObserver(observer);
            }
        });
        if (mValue != null) observer.onChanged(mValue);
    }

    /**
     * @param observer 需要移除的观察者
     */
    public void removeObserver(Observer<? super T> observer) {
        mStore.remove(observer);
    }

    /**
     * 向Observer推送Value的新状态
     */
    protected void start() {
        for (Map.Entry<Observer<? super T>, Lifecycle> entry : mStore.entrySet()) {
            if (entry.getValue() == null) {
                entry.getKey().onChanged(mValue);
                continue;
            }
            if (!entry.getValue().event.exceedTo(Lifecycle.Event.ON_STOP)) {
                entry.getKey().onChanged(mValue);
            }
        }
    }

}

