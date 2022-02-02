package mandysax.navigation;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

/**
 * 用于查找导航
 *
 * @author ZXHHYJ
 */
public final class Navigation {

    /**
     * @param view 通过哪个view开始查找
     * @return 找到的距离最近的导航控制器
     */
    @NonNull
    public static NavController findViewNavController(View view) {
        if (!(view instanceof ViewGroup)) {
            return findViewNavController(((View) view.getParent()));
        }
        ViewGroup parent = (ViewGroup) view;
        while (parent != null) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) instanceof NavHostFragmentView) {
                    NavHostFragmentView navHostFragmentView = (NavHostFragmentView) parent.getChildAt(i);
                    if (navHostFragmentView.getNavHostFragment().isVisible())
                        return navHostFragmentView.getNavHostFragment().getNavController();
                }
            }
            if (parent.getParent() != null && parent.getParent() instanceof ViewGroup)
                parent = (ViewGroup) parent.getParent();
        }
        throw new NullPointerException("No NavHostFragment found available above view");
    }

}
