package mandysax.core.app;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ZXHHYJ
 */
public abstract class OnBackPressedCallback {

    private boolean mEnabled;
    private final CopyOnWriteArrayList<Cancellable> mCancellables = new CopyOnWriteArrayList<>();

    public OnBackPressedCallback(boolean enabled) {
        mEnabled = enabled;
    }

    public final void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public final boolean isEnabled() {
        return mEnabled;
    }

    public final void remove() {
        for (Cancellable cancellable : mCancellables) {
            cancellable.cancel();
        }
    }

    public abstract void handleOnBackPressed();

    void addCancellable(Cancellable cancellable) {
        mCancellables.add(cancellable);
    }

    void removeCancellable(Cancellable cancellable) {
        mCancellables.remove(cancellable);
    }
}
