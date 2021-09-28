package mandysax.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * @author liuxiaoliu66
 */
public final class LayoutInflaterCompat {

    static class Factory2Wrapper implements LayoutInflater.Factory2 {

        final LayoutInflaterFactory mFactory;

        Factory2Wrapper(LayoutInflaterFactory factory) {
            mFactory = factory;
        }

        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            return mFactory.onCreateView(parent, name, context, attrs);
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return mFactory.onCreateView(null, name, context, attrs);
        }
    }

    private LayoutInflaterCompat() {
    }

    public static void setFactory2(@NonNull LayoutInflater inflater, LayoutInflaterFactory factory) {
        if (inflater.getFactory2() == null) {
            inflater.setFactory2(new Factory2Wrapper(factory));
        }
    }

}
