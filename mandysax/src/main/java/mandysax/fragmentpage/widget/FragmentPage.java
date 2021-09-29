package mandysax.fragmentpage.widget;

import android.content.Context;
import android.util.AttributeSet;

import mandysax.fragment.Fragment;
import mandysax.lifecycle.ViewModelProviders;

/**
 * @author liuxiaoliu66
 */
public class FragmentPage extends mandysax.fragment.FragmentView {

    public interface Adapter {
        <T extends Fragment> T onCreateFragment(int position);
    }

    private Adapter mAdapter;

    private FragmentPageStateViewModel mStateViewModel;

    public <T extends Adapter> void setAdapter(T adapter) {
        mAdapter = adapter;
    }

    private int mPosition = -1;

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
        mPosition = position;
        if (mStateViewModel == null) {
            mStateViewModel = ViewModelProviders.of(getActivity()).get(FragmentPageStateViewModel.class);
        }
    }

    public int getPosition() {
        return mPosition;
    }


}
