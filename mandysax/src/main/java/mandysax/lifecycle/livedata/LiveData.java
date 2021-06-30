package mandysax.lifecycle.livedata;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.CopyOnWriteArrayList;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;

/*
 *fix bug form 21.5.23
 */
public abstract class LiveData<T> extends Handler {

    public LiveData() {
        super(Looper.getMainLooper());//android11中已废弃了无参的handler
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        ((LiveData) msg.obj).start();
    }

    protected T content;

    protected final CopyOnWriteArrayList<Observer<? super T>> callBack = new CopyOnWriteArrayList<>();

    public T getValue() {
        return content;
    }

    public LiveData<T> lazy(LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
        if (getValue() == null) {
            Observer<T> tObserver = new Observer<T>() {
                @Override
                public void onChanged(T p2) {
                    if (content != null) {
                        observer.onChanged(content);
                        removeObserver(this);
                    }
                }
            };
            observeForever(tObserver);
            lifecycleOwner.getLifecycle().addObserver(state -> {
                if (state == Lifecycle.Event.ON_DESTROY)
                    removeObserver(tObserver);
            });
        } else observer.onChanged(getValue());
        return this;
    }

    public LiveData<T> lazy(Observer<? super T> observer) {
        if (getValue() == null)
            observeForever(new Observer<T>() {
                @Override
                public void onChanged(T p2) {
                    if (content != null) {
                        observer.onChanged(content);
                        removeObserver(this);
                    }
                }
            });
        else observer.onChanged(getValue());
        return this;
    }

    public LiveData<T> observeForever(Observer<? super T> observer) {
        callBack.add(observer);
        return this;
    }

    public LiveData<T> observe(LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
        lifecycleOwner.getLifecycle().addObserver(state -> {
            if (state == Lifecycle.Event.ON_DESTROY) {
                removeObserver(observer);
            }
        });
        observeForever(observer);
        return this;
    }

    public void removeObserver(Observer<? super T> observer) {
        callBack.remove(observer);
    }

    protected void start() {
        for (Observer<? super T> c : callBack)
            c.onChanged(content);
    }

}

