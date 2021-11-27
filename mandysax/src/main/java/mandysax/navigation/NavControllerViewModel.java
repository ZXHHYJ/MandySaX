package mandysax.navigation;

import androidx.annotation.Nullable;

import java.util.Deque;
import java.util.LinkedList;

import mandysax.fragment.Fragment;
import mandysax.lifecycle.ViewModel;

/**
 * 用于存储导航的返回栈信息
 * @author ZXHHYJ
 */
public final class NavControllerViewModel extends ViewModel {

    /**
     * 记录fragment
     */
    private final Deque<Fragment> mFragments = new LinkedList<>();

    private Fragment mNowFragment;

    /**
     * 获取最后一个fragment
     *
     * @return fragment
     */
    @Nullable
    public Fragment getLastFragment() {
        if (mFragments.isEmpty()) return null;
        return mFragments.getLast();
    }

    public Fragment getNowFragment() {
        return mNowFragment;
    }

    /**
     * 添加到返回栈
     *
     * @param fragment 需要添加的fragment
     */
    public void add(Fragment fragment) {
        if (mNowFragment != null)
            mFragments.addLast(mNowFragment);
        mNowFragment = fragment;
    }

    /**
     * 移除当前最后一个fragment
     */
    public void removeLast() {
        mNowFragment = getLastFragment();
        mFragments.removeLast();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mFragments.clear();
        mNowFragment = null;
    }
}
