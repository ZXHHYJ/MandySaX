package mandysax.lifecycle.livedata;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;
import mandysax.lifecycle.LifecycleOwner;

/**
 * @author ZXHHYJ
 * @param <T> 需要包装的泛型类型
 */
public class LiveData<T> {

    public LiveData() {
    }

    public LiveData(T value) {
        mValue = value;
    }

    private Handler mHandler;

    private volatile T mValue;

    private final ArrayList<ObserverWrapper> mObservers = new ArrayList<>();

    void postValue(T value) {
        if (isMainThread()) {
            setValue(value);
            return;
        }
        if (mHandler == null) {
            synchronized (MutableLiveData.class) {
                if (mHandler == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        mHandler.post(() -> {
            mValue = value;
            start();
        });
    }

    void setValue(T value) {
        if (!isMainThread()) {
            throw new IllegalStateException("Cannot invoke setValue on a background thread");
        }
        mValue = value;
        start();
    }

    private boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

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
                if (p2 != null) {
                    observer.onChanged(p2);
                    removeObserver(this);
                }
            }
        });
    }

    /**
     * @param observer 观察者
     */
    public void observeForever(Observer<? super T> observer) {
        Observer<? super T> existing = ifAbsent(observer);
        if (existing != null) {
            return;
        }
        mObservers.add(new AlwaysActiveObserver(observer));
        if (mValue != null) {
            observer.onChanged(mValue);
        }
    }

    /**
     * @param owner    生命周期所有者
     * @param observer 观察者
     */
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (owner.getLifecycle().event == Lifecycle.Event.ON_DESTROY) {
            // ignore
            return;
        }
        Observer<? super T> existing = ifAbsent(observer);
        if (existing != null) {
            return;
        }
        mObservers.add(new LifecycleBoundObserver(owner, observer));
        if (mValue != null) {
            observer.onChanged(mValue);
        }
    }

    @Nullable
    private Observer<? super T> ifAbsent(Observer<? super T> observer) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            ObserverWrapper wrapper = mObservers.get(i);
            if (wrapper.mObserver.equals(observer)) {
                return observer;
            }
        }
        return null;
    }

    /**
     * @param observer 需要移除的观察者
     */
    public void removeObserver(Observer<? super T> observer) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            ObserverWrapper wrapper = mObservers.get(i);
            if (wrapper.mObserver.equals(observer)) {
                mObservers.remove(wrapper);
                return;
            }
        }
    }

    /**
     * 向Observer推送Value的新状态
     */
    protected void start() {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            ObserverWrapper wrapper = mObservers.get(i);
            if (wrapper.isActive()) {
                wrapper.mObserver.onChanged(mValue);
            } else if (wrapper instanceof LiveData.LifecycleBoundObserver) {
                LiveData<T>.LifecycleBoundObserver lifecycleBoundObserver = ((LiveData<T>.LifecycleBoundObserver) wrapper);
                lifecycleBoundObserver.mOwner.getLifecycle().addObserver(new LifecycleObserver() {
                    @Override
                    public void observer(Lifecycle.Event state) {
                        switch (state) {
                            case ON_CREATE:
                            case ON_START:
                            case ON_RESUME:
                            case ON_RESTART:
                                wrapper.mObserver.onChanged(mValue);
                                lifecycleBoundObserver.mOwner.getLifecycle().removeObserver(this);
                        }
                    }
                });
            }
        }
    }

    private abstract class ObserverWrapper {
        final Observer<? super T> mObserver;

        ObserverWrapper(Observer<? super T> mObserver) {
            this.mObserver = mObserver;
        }

        abstract boolean isActive();
    }

    private class LifecycleBoundObserver extends ObserverWrapper implements LifecycleObserver {

        private final LifecycleOwner mOwner;

        private boolean mActive = false;

        LifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<? super T> observer) {
            super(observer);
            mOwner = owner;
            mOwner.getLifecycle().addObserver(this);
        }

        @Override
        public void observer(@NonNull Lifecycle.Event state) {
            switch (state) {
                case ON_CREATE:
                case ON_PAUSE:
                case ON_STOP:
                    mActive = false;
                    break;
                case ON_DESTROY:
                    mActive = false;
                    mOwner.getLifecycle().removeObserver(this);
                    mObservers.remove(this);
                    break;
                default:
                    mActive = true;
                    break;
            }
        }

        @Override
        public boolean isActive() {
            return mActive;
        }
    }

    private class AlwaysActiveObserver extends ObserverWrapper {

        AlwaysActiveObserver(Observer<? super T> observer) {
            super(observer);
        }

        @Override
        boolean isActive() {
            return true;
        }

    }

}

