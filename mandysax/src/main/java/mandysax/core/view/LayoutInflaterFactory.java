package mandysax.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author liuxiaoliu66
 */
public interface LayoutInflaterFactory {

    View onCreateView(View parent, String name, Context context, AttributeSet attrs);
}
