package mandysax.app;

import android.app.Activity;

import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleImpl;
import mandysax.lifecycle.LifecycleOwner;

public class LifecycleActivity extends Activity implements LifecycleOwner {
    private final Lifecycle mLifecycle = new Lifecycle();

    private boolean mIsOnCreate = false;

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
    }

    @Override
    public LifecycleImpl getLifecycle() {
        return mLifecycle;
    }
}
