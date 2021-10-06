package mandysax.fragment;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleOwner;
import mandysax.lifecycle.LifecycleRegistry;
import mandysax.lifecycle.ViewModelStore;
import mandysax.lifecycle.ViewModelStoreImpl;
import mandysax.lifecycle.ViewModelStoreOwner;

public final class FragmentViewLifecycleOwner implements LifecycleOwner, ViewModelStoreOwner {

    private final LifecycleRegistry mLifecycle = new LifecycleRegistry();
    private final ViewModelStoreImpl mViewModelStore = new ViewModelStoreImpl();

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
    public ViewModelStore getViewModelStore() {
        return mViewModelStore;
    }

}
