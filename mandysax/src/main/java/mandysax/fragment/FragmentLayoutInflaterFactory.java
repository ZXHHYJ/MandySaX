package mandysax.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import mandysax.core.view.LayoutInflaterFactory;

/**
 * @author liuxiaoliu66
 */
public final class FragmentLayoutInflaterFactory implements LayoutInflaterFactory {

    public static final String FRAGMENT = "fragment";

    public FragmentLayoutInflaterFactory() {
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (FRAGMENT.equalsIgnoreCase(name)) {
            return new FragmentView(context, attrs);
        }
        return null;
    }

}
