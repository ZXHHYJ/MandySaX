package mandysax.lifecycle.livedata;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import mandysax.lifecycle.LifecycleOwner;

/**
 * @author liuxiaoliu66
 */
public class MutableLiveData<T> extends LiveData<T> {

    private Handler mHandler;

    public MutableLiveData(T value) {
        content = value;
    }

    public MutableLiveData() {
    }

    @Override
    public void lazy(Observer<? super T> observer) {
        super.lazy(observer);
    }

    @Override
    public void observeForever(Observer<? super T> observer) {
        super.observeForever(observer);
    }

    @Override
    public void observe(@NonNull LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
        super.observe(lifecycleOwner, observer);
    }

    public void postValue(T value) {
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
        mHandler.post(() -> setValue(value));
    }

    public void setValue(T value) {
        if (!isMainThread()) {
            throw new IllegalStateException("Cannot invoke setValue on a background thread");
        }
        content = value;
        start();
    }

    private boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

}

