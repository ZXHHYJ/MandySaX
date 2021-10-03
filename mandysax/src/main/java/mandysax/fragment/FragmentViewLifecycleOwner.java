package mandysax.fragment;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;
import mandysax.lifecycle.ViewModelStore;
import mandysax.lifecycle.ViewModelStoreImpl;
import mandysax.lifecycle.ViewModelStoreOwner;

public final class FragmentViewLifecycleOwner implements LifecycleOwner, ViewModelStoreOwner {

    private final Lifecycle mLifecycle = new Lifecycle();
    private final ViewModelStore mViewModelStore = new ViewModelStore();

    FragmentViewLifecycleOwner() {
        mLifecycle.addObserver(state -> {
            if (state == Lifecycle.Event.ON_DESTROY) {
                mViewModelStore.clear();
            }
        });
    }

    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    @Override
    public ViewModelStoreImpl getViewModelStore() {
        return mViewModelStore;
    }

}
