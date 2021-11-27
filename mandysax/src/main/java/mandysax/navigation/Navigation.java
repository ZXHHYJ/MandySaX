package mandysax.navigation;

import android.view.View;
import android.view.ViewGroup;

/**
 * 用于查找导航
 * @author ZXHHYJ
 */
public final class Navigation {

    /**
     * @param view 通过哪个view开始查找
     * @return 找到的距离最近的导航控制器
     */
    public static NavController findViewNavController(View view) {
        if (!(view instanceof ViewGroup)) {
            return findViewNavController(((View) view.getParent()));
        }
        ViewGroup viewGroup = (ViewGroup) view;
        while (true) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof NavHostFragmentView) {
                    NavHostFragmentView navHostFragmentView = (NavHostFragmentView) viewGroup.getChildAt(i);
                    if (navHostFragmentView.getVisibility() != View.VISIBLE) {
                        continue;
                    }
                    return navHostFragmentView.getNavHostFragment().getNavController();
                }
                viewGroup = (ViewGroup) viewGroup.getParent();
            }
        }
    }

}
