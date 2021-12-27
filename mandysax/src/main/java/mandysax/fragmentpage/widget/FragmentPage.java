package mandysax.fragmentpage.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.HashMap;

import mandysax.fragment.Fragment;
import mandysax.fragment.FragmentManager;
import mandysax.fragment.FragmentTransaction;
import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;

/**
 * @author liuxiaoliu66
 */
public class FragmentPage extends mandysax.fragment.FragmentView {

    public interface Adapter {
        /**
         * 创建Fragment时回调
         *
         * @param position Fragment的页数
         * @return 创建的Fragment
         */
        Fragment onCreateFragment(int position);
    }

    private Adapter mAdapter;

    public <T extends Adapter> void setAdapter(T adapter) {
        mAdapter = adapter;
    }

    public FragmentPage(Context context) {
        super(context);
        setOnHierarchyChangeListener(null);
    }

    public FragmentPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnHierarchyChangeListener(null);
    }

    public void setPosition(int position) {
        if (mAdapter == null) {
            throw new NullPointerException("FragmentPage.Adapter is null");
        }
        if (position == getFragment().position) {
            return;
        }
        getFragment().position = position;
        if (getFragment().tags == null) {
            getFragment().tags = new HashMap<>(position * position);
        }
        String tag = getFragment().tags.get(position);
        FragmentManager fragmentPlusManager = getActivity().getFragmentPlusManager();
        FragmentTransaction fragmentTransaction = fragmentPlusManager.beginTransaction();
        if (fragmentPlusManager.findFragmentByTag(tag) == null) {
            Fragment fragment;
            fragment = mAdapter.onCreateFragment(position);
            fragment.getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleObserver() {
                @Override
                public void observer(Lifecycle.Event state) {
                    if (state == Lifecycle.Event.ON_CREATE) {
                        getFragment().tags.put(position, getFragment().lifoTag = fragment.getTag());
                        fragment.getViewLifecycleOwner().getLifecycle().removeObserver(this);
                    }
                }
            });
            if (getFragment().lifoTag != null) {
                fragmentTransaction.hide(fragmentPlusManager.findFragmentByTag(getFragment().lifoTag));
            }
            fragmentTransaction.add(getId(), fragment);
        } else {
            Fragment fragment = fragmentPlusManager.findFragmentByTag(tag);
            if (getFragment().lifoTag != null) {
                fragmentTransaction.hide(fragmentPlusManager.findFragmentByTag(getFragment().lifoTag));
            }
            if (!fragment.isAdded()) {
                fragmentTransaction.add(getId(), fragment);
            } else {
                fragmentTransaction.show(fragment);
            }
            getFragment().lifoTag = fragment.getTag();
        }
        fragmentTransaction.commitNow();
    }

    @Override
    public StateViewModel getFragment() {
        return (StateViewModel) super.getFragment();
    }

    @Override
    public String getName() {
        return StateViewModel.class.getCanonicalName();
    }

    public int getPosition() {
        return getFragment().position;
    }

}
