package mandysax.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public final class FragmentLayoutInflaterFactory implements LayoutInflater.Factory2 {

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (name.equalsIgnoreCase("fragment")) {
            return new FragmentView(context, attrs);
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    FragmentLayoutInflaterFactory() {
    }

}
