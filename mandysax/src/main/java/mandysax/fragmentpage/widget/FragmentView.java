package mandysax.fragmentpage.widget;

import android.content.Context;

import mandysax.fragment.Fragment;

public class FragmentView extends mandysax.fragment.FragmentView {

    private final Fragment mFragment;

    public FragmentView(Context context, Fragment fragment) {
        super(context);
        mFragment = fragment;
    }

    @Override
    public Fragment getFragment() {
        return mFragment;
    }

}
