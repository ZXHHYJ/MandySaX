package mandysax.lifecycle;

import androidx.annotation.NonNull;

/**
 * @author ZXHHYJ
 */
public interface LifecycleObserver {
    /**
     * state changed
     * @param state The Lifecycle.Event corresponding to the lifecycle event
     */
    void observer(@NonNull Lifecycle.Event state);
}
