package studio.mandysa.music.ui.fragment;

import android.os.Bundle;
import android.view.View;

import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseLoadingFragment;

public class SingerInfoFragment extends BaseLoadingFragment {

    private final int mId;

    public SingerInfoFragment(int id) {
        mId = id;
    }

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int onCreateView() {
        return R.layout.singer_info;
    }
}
