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
        Fragment onCreateFragment(int position);
    }

    private Adapter mAdapter;

    public <T extends Adapter> void setAdapter(T adapter) {
        mAdapter = adapter;
    }

    public FragmentPage(Context context) {
        super(context);
    }

    public FragmentPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPosition(int position) {
        if (mAdapter == null) {
            throw new NullPointerException("FragmentPage.Adapter is null");
        }
        if (position == getFragment().position) return;
        getFragment().position = position;
        if (getFragment().tags == null) {
            getFragment().tags = new HashMap<>();
        }
        String tag = getFragment().tags.get(position);
        FragmentManager fragmentPlusManager = getActivity().getFragmentPlusManager();
        Fragment fragment;
        if (fragmentPlusManager.findFragmentByTag(tag) == null) {
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
            FragmentTransaction fragmentTransaction = fragmentPlusManager.beginTransaction();
            if (getFragment().lifoTag != null)
                fragmentTransaction.hide(fragmentPlusManager.findFragmentByTag(getFragment().lifoTag));
            fragmentTransaction.add(getId(), fragment);
            fragmentTransaction.commit();
        } else {
            Fragment fragment1 = fragmentPlusManager.findFragmentByTag(tag);
            FragmentTransaction fragmentTransaction = fragmentPlusManager.beginTransaction();
            if (getFragment().lifoTag != null) {
                fragmentTransaction.hide(fragmentPlusManager.findFragmentByTag(getFragment().lifoTag));
            }
            if (!fragment1.isAdded())
                fragmentTransaction.add(getId(), fragment1);
            else
                fragmentTransaction.show(fragment1);
            getFragment().lifoTag = fragment1.getTag();
            fragmentTransaction.commit();
        }
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
