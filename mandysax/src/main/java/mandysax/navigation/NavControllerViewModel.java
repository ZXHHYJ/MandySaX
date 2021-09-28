package mandysax.navigation;

import java.util.Deque;
import java.util.LinkedList;

import mandysax.fragment.Fragment;
import mandysax.lifecycle.ViewModel;

/**
 * 用于存储导航的返回栈信息
 */
public final class NavControllerViewModel extends ViewModel {

    /**
     * 记录fragment
     */
    private final Deque<Fragment> mFragments = new LinkedList<>();

    /**
     * 记录返回栈id
     */
    private final Deque<Integer> mBackStacks = new LinkedList<>();

    /**
     * 获取最后一个fragment
     *
     * @return fragment
     */
    public Fragment getLastFragment() {
        if (mFragments.isEmpty()) return null;
        return mFragments.getLast();
    }

    /**
     * 获取最后一个返回栈id
     *
     * @return id
     */
    public int getBackStackIndexLast() {
        return mBackStacks.getLast();
    }

    /**
     * 添加到返回栈
     *
     * @param fragment       需要添加的fragment
     * @param backStackIndex 对应返回栈id
     */
    public void add(Fragment fragment, int backStackIndex) {
        mFragments.addLast(fragment);
        mBackStacks.addLast(backStackIndex);
    }

    /**
     * 移除当前最后一个fragment和返回栈id
     */
    public void removeLast() {
        mFragments.removeLast();
        mBackStacks.removeLast();
    }

    /**
     * 获取返回栈大小
     *
     * @return 返回栈大小
     */
    public int getBackStackEntryCount() {
        return mBackStacks.size();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mFragments.clear();
        mBackStacks.clear();
    }
}
