package studio.mandysa.music.ui.fragment;

import android.media.MediaMetadata;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import mandysax.anna2.callback.Callback;
import studio.mandysa.music.service.Media;
import studio.mandysa.music.service.PlayManager;
import studio.mandysa.music.R;
import studio.mandysa.music.logic.Model.MusicModel;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Model.SearchMusicModel;
import studio.mandysa.music.logic.Network.ServiceCreator;
import studio.mandysa.music.ui.base.BaseLoadingFragment;
import studio.mandysa.music.ui.viewholder.SongViewHolder;
import studio.mandysa.music.ui.viewmodel.SearchViewModel;
import studio.mandysa.music.utils.ImageLoader;
import studio.mandysa.music.utils.TextViewUtils;

public class SearchMusicFragment extends BaseLoadingFragment {

    private final ArrayList<SearchMusicModel> mList = new ArrayList<>();

    private final SongListAdapter mAdapter = new SongListAdapter();

    private SearchViewModel mViewModel;

    private RecyclerView mRvList;

    private int mPage = 1;

    private void init() {

        mViewModel = getViewModel(SearchViewModel.class);

        mRvList = findViewById(R.id.rv_searchMusicAndSinger_view);

        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvList.setHasFixedSize(true);
        mRvList.setAdapter(mAdapter);

        nextPage();

        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    nextPage();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        nextPage();
    }

    private void nextPage() {
        ServiceCreator.create(NeteaseCloudMusicApi.class).searchMusic(mViewModel.getSearchContent().getValue(), (mPage - 1) * 30).set(new Callback<SearchMusicModel>() {

            @Override
            public void onResponse(boolean loaded, SearchMusicModel t) {
                mList.add(t);
                mAdapter.notifyItemInserted(mList.size());
                if (!loaded) return;
                if (mPage == 1)
                    hide();
                mPage++;
            }

            @Override
            public void onFailure(int code) {
                if (code != 200)
                    show();
            }

        });
    }

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    protected void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mViewModel.getSearchContent().observe(this, p1 -> {
            mPage = 1;
            if (mRvList.getChildCount() > 0) {
                mRvList.removeAllViews();
                mList.clear();
                mAdapter.notifyDataSetChanged();
            }
            onRefresh();
        });
    }

    @Override
    protected int onCreateView() {
        return R.layout.search_music_and_singer;
    }

    public class SongListAdapter extends RecyclerView.Adapter<SongViewHolder> {

        @Override
        public void onBindViewHolder(@NonNull final SongViewHolder p1, final int p2) {
            MediaMetadata mediaMetadata = mList.get(p2).build();
            if (mList.get(p2).picUrl != null)
                ImageLoader.getInstance().load(mList.get(p2).picUrl).into(p1.cover);
            else
                ServiceCreator.create(NeteaseCloudMusicApi.class).getMusicInfo(mediaMetadata.getString(Media.MEDIA_ID)).set(new Callback<MusicModel>() {

                    @Override
                    public void onResponse(boolean loaded, MusicModel t) {
                        ImageLoader.getInstance().load(mList.get(p2).picUrl = t.picUrl).into(p1.cover);
                    }

                    @Override
                    public void onFailure(int code) {
                    }
                });
            p1.songName.setText(mediaMetadata.getString(Media.TITLE));
            p1.singerName.setText(mediaMetadata.getString(Media.ARTIST));
            TextViewUtils.markByColor(p1.songName, mViewModel.getSearchContent().getValue());
            TextViewUtils.markByColor(p1.singerName, mViewModel.getSearchContent().getValue());
            p1.itemView.setOnClickListener(p11 -> {
                ArrayList<MediaMetadata> playList = new ArrayList<>();
                for (SearchMusicModel smm : mList) {
                    playList.add(smm.build());
                }
                PlayManager.getInstance(getActivity()).playMedia(playList, p2);
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SongViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.song_item, parent, false));
        }

        public int getItemCount() {
            return mList.size();
        }

    }

}
