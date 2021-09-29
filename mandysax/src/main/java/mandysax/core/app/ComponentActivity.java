package mandysax.core.app;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Iterator;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;
import mandysax.lifecycle.LifecycleOwner;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.ViewModelProviders;
import mandysax.lifecycle.ViewModelStore;
import mandysax.lifecycle.ViewModelStoreImpl;
import mandysax.lifecycle.ViewModelStoreOwner;

/**
 * @author liuxiaoliu66
 */
public class ComponentActivity extends Activity implements LifecycleOwner, ViewModelStoreOwner {
    private final Lifecycle mLifecycle = new Lifecycle();
    private ViewModelStore mViewModelStore;
    private NonConfigurationInstances mLastNonConfigurationInstances = null;

    private boolean mIsOnCreate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOnBackPressedDispatcher().mFallbackOnBackPressed = ComponentActivity.super::onBackPressed;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycle.dispatchOnStart();
        if (!mIsOnCreate) {
            mLifecycle.dispatchOnCreate();
            mIsOnCreate = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLifecycle.dispatchOnRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycle.dispatchOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifecycle.dispatchOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycle.dispatchOnStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycle.dispatchOnDestroy();
        mIsOnCreate = false;
        getOnBackPressedDispatcher().mFallbackOnBackPressed = null;
    }

    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }

    @Override
    public void onBackPressed() {
        getOnBackPressedDispatcher().onBackPressed();
    }

    public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return ViewModelProviders.of(this).get(OnBackPressedDispatcher.class);
    }

    @Override
    public ViewModelStoreImpl getViewModelStore() {
        if (mViewModelStore == null) {
            getLastNonConfigurationInstance();
            if (mViewModelStore == null) {
                mViewModelStore = new ViewModelStore();
            }
            return mViewModelStore;
        }
        return mViewModelStore;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        if (mLastNonConfigurationInstances == null) {
            NonConfigurationInstances nci = new NonConfigurationInstances();
            nci.viewModelStore = mViewModelStore == null ? new ViewModelStore() : mViewModelStore;
            return mLastNonConfigurationInstances = nci;
        } else {
            return mLastNonConfigurationInstances;
        }
    }

    @Override
    public Object getLastNonConfigurationInstance() {
        NonConfigurationInstances nci = (NonConfigurationInstances) super.getLastNonConfigurationInstance();
        if (nci == null) {
            return null;
        }
        if (mViewModelStore == null) {
            mViewModelStore = nci.viewModelStore;
        }
        return nci;
    }

    public static class OnBackPressedDispatcher extends ViewModel {
        Runnable mFallbackOnBackPressed;

        private final ArrayDeque<OnBackPressedCallback> mOnBackPressedCallbacks = new ArrayDeque<>();

        public void addCallback(OnBackPressedCallback onBackPressedCallback) {
            addCancellableCallback(onBackPressedCallback);
        }

        public void addCallback(@NonNull LifecycleOwner owner,
                                OnBackPressedCallback onBackPressedCallback) {
            Lifecycle lifecycle = owner.getLifecycle();
            if (lifecycle.event == Lifecycle.Event.ON_DESTROY) {
                return;
            }

            onBackPressedCallback.addCancellable(
                    new LifecycleOnBackPressedCancellable(lifecycle, onBackPressedCallback));
        }

        public boolean hasEnabledCallbacks() {
            Iterator<OnBackPressedCallback> iterator =
                    mOnBackPressedCallbacks.descendingIterator();
            while (iterator.hasNext()) {
                if (iterator.next().isEnabled()) {
                    return true;
                }
            }
            return false;
        }

        public void onBackPressed() {
            Iterator<OnBackPressedCallback> iterator =
                    mOnBackPressedCallbacks.descendingIterator();
            while (iterator.hasNext()) {
                OnBackPressedCallback callback = iterator.next();
                if (callback.isEnabled()) {
                    callback.handleOnBackPressed();
                    return;
                }
            }
            if (mFallbackOnBackPressed != null) {
                mFallbackOnBackPressed.run();
            }
        }

        Cancellable addCancellableCallback(OnBackPressedCallback onBackPressedCallback) {
            mOnBackPressedCallbacks.add(onBackPressedCallback);
            OnBackPressedCancellable cancellable = new OnBackPressedCancellable(onBackPressedCallback);
            onBackPressedCallback.addCancellable(cancellable);
            return cancellable;
        }

        private class OnBackPressedCancellable implements Cancellable {
            private final OnBackPressedCallback mOnBackPressedCallback;

            OnBackPressedCancellable(OnBackPressedCallback onBackPressedCallback) {
                mOnBackPressedCallback = onBackPressedCallback;
            }

            @Override
            public void cancel() {
                mOnBackPressedCallbacks.remove(mOnBackPressedCallback);
                mOnBackPressedCallback.removeCancellable(this);
            }
        }

        private class LifecycleOnBackPressedCancellable implements LifecycleObserver,
                Cancellable {
            private final Lifecycle mLifecycle;
            private final OnBackPressedCallback mOnBackPressedCallback;

            private Cancellable mCurrentCancellable;

            LifecycleOnBackPressedCancellable(@NonNull Lifecycle lifecycle,
                                              OnBackPressedCallback onBackPressedCallback) {
                mLifecycle = lifecycle;
                mOnBackPressedCallback = onBackPressedCallback;
                lifecycle.addObserver(this);
            }


            @Override
            public void cancel() {
                mLifecycle.removeObserver(this);
                mOnBackPressedCallback.removeCancellable(this);
                if (mCurrentCancellable != null) {
                    mCurrentCancellable.cancel();
                    mCurrentCancellable = null;
                }
            }

            @Override
            public void Observer(Lifecycle.Event state) {
                if (state == Lifecycle.Event.ON_START) {
                    mCurrentCancellable = addCancellableCallback(mOnBackPressedCallback);
                } else if (state == Lifecycle.Event.ON_STOP) {
                    // Should always be non-null
                    if (mCurrentCancellable != null) {
                        mCurrentCancellable.cancel();
                    }
                } else if (state == Lifecycle.Event.ON_DESTROY) {
                    cancel();
                }
            }
        }
    }

    private static final class NonConfigurationInstances {
        ViewModelStore viewModelStore;//ViewModel的管理者
    }

}