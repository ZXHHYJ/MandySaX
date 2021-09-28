package mandysax.core.view;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * @author liuxiaoliu66
 */
public final class ViewCompat {
    public static void setOnApplyWindowInsetsListener(@NonNull View view, OnApplyWindowInsetsListener listener) {
        view.setOnApplyWindowInsetsListener((v, insets) -> {
            listener.onApplyWindowInsets(v, new WindowInsetsCompat(insets));
            return insets;
        });
    }
}
