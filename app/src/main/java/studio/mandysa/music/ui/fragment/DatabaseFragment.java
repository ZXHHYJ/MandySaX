package studio.mandysa.music.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseFragment;
import studio.mandysa.music.ui.viewmodel.DatabaseViewModel;

public class DatabaseFragment extends BaseFragment {

    private DatabaseViewModel mViewModel;

    private RecyclerView mRvMusicList;

    @Override
    protected int onCreateView() {
        return R.layout.database;
    }

    @Override
    protected void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        mViewModel = getViewModel(DatabaseViewModel.class);
        mRvMusicList = findViewById(R.id.rv_database_music_list);
    }



}
