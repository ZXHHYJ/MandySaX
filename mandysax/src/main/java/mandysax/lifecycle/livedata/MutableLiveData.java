package mandysax.lifecycle.livedata;

import androidx.annotation.NonNull;

import mandysax.lifecycle.LifecycleOwner;

/**
 * @author liuxiaoliu66
 */
public class MutableLiveData<T> extends LiveData<T> {

    public MutableLiveData(T value) {
        super(value);
    }

    public MutableLiveData() {
        super();
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
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
    }

    @Override
    public void postValue(T value) {
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        super.setValue(value);
    }


}

