package mandysax.navigation;

import android.view.View;
import android.view.ViewGroup;

import mandysax.fragment.FragmentTransaction;
import mandysax.fragment.FragmentView;
import mandysax.navigation.fragment.NavHostFragment;

/**
 * 用于查找导航
 */
public final class Navigation {

    /**
     * @param view 通过哪个view开始查找
     * @return 找到的距离最近的导航控制器
     */
    public static NavController findViewNavController(View view) {
        return findViewNavController(view, null);
    }

    /**
     * @param view                通过哪个view开始查找
     * @param fragmentTransaction 自定义事务
     * @return 找到的距离最近的导航控制器
     */
    public static NavController findViewNavController(View view, FragmentTransaction fragmentTransaction) {
        ViewGroup viewGroup = view instanceof ViewGroup ? (ViewGroup) view : (ViewGroup) view.getParent();
        while (viewGroup != null) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof FragmentView) {
                    FragmentView fragmentView = (FragmentView) viewGroup.getChildAt(i);
                    if (fragmentView.getFragment() != null && fragmentView.getFragment() instanceof NavHostFragment) {
                        NavHostFragment navHostFragment = (NavHostFragment) fragmentView.getFragment();
                        return fragmentTransaction == null ? navHostFragment.getNavController() : navHostFragment.getNavController(fragmentTransaction);
                    }
                }
            }
            viewGroup = (ViewGroup) viewGroup.getParent();
        }
        throw new NullPointerException("The RootView of this view has no navigation");
    }

}
