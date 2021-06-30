package mandysax.lifecycle.livedata;

import android.os.Message;

import mandysax.lifecycle.LifecycleOwner;

public class MutableLiveData<T> extends LiveData<T> {

    public MutableLiveData(T value) {
        content = value;
    }

    public MutableLiveData() {
    }

    @Override
    public MutableLiveData<T> lazy(Observer<? super T> observer) {
        return (MutableLiveData<T>) super.lazy(observer);
    }

    @Override
    public MutableLiveData<T> observeForever(Observer<? super T> observer) {
        return (MutableLiveData<T>) super.observeForever(observer);
    }

    @Override
    public MutableLiveData<T> observe(LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
        return (MutableLiveData<T>) super.observe(lifecycleOwner, observer);
    }

    public T postValue(T value) {
        content = value;
        Message message = new Message();
        message.obj = this;
        sendMessage(message);
        return value;
    }

    public T setValue(T value) {
        content = value;
        start();
        return value;
    }

}

