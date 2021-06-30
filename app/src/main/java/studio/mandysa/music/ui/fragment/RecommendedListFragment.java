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
import studio.mandysa.music.service.PlayManager;
import mandysax.navigation.widget.Navigation;
import studio.mandysa.music.R;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Model.NewSongModel;
import studio.mandysa.music.logic.Model.PlaylistModel;
import studio.mandysa.music.logic.Network.ServiceCreator;
import studio.mandysa.music.ui.base.BaseLoadingFragment;
import studio.mandysa.music.ui.viewholder.PlaylistViewHolder;
import studio.mandysa.music.ui.viewholder.SongViewHolder;
import studio.mandysa.music.ui.viewmodel.LoginViewModel;
import studio.mandysa.music.utils.ImageLoader;

public class RecommendedListFragment extends BaseLoadingFragment {

    private LoginViewModel mViewModel;

    private final ArrayList<PlaylistModel> mPlaylistList = new ArrayList<>();

    private final PlaylistAdapter mPlaylistAdapter = new PlaylistAdapter(mPlaylistList);

    private final ArrayList<NewSongModel> mNewSongList = new ArrayList<>();

    private final NewSongAdapter mNewSongAdapter = new NewSongAdapter(mNewSongList);

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = getViewModel(LoginViewModel.class);
        initPlaylist();
        initNewSong();
        initRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        initRefresh();
    }

    public void initRefresh() {
        (mViewModel.getCookie() == null ? ServiceCreator.create(NeteaseCloudMusicApi.class).getRecommendedPlaylist() : ServiceCreator.create(NeteaseCloudMusicApi.class).getRecommendedPlaylist(mViewModel.getCookie())).set(new Callback<PlaylistModel>() {

            @Override
            public void onResponse(boolean loaded, PlaylistModel t) {
                mPlaylistList.add(t);
                if (loaded) {
                    mPlaylistAdapter.notifyDataSetChanged();
                    // mSkeletonPlaylistList.hide();
                    hide();
                }
            }

            @Override
            public void onFailure(int code) {
                show();
            }
        });
        (ServiceCreator.create(NeteaseCloudMusicApi.class).getRecommendedSong()).set(new Callback<NewSongModel>() {

            @Override
            public void onResponse(boolean loaded, NewSongModel t) {
                mNewSongList.add(t);
                if (loaded) {
                    mNewSongAdapter.notifyDataSetChanged();
                    // mSkeletonNewSongList.hide();
                    hide();
                }
            }

            @Override
            public void onFailure(int code) {
                show();
            }
        });
    }

    void initPlaylist() {
        RecyclerView mRvPlaylist = findViewById(R.id.rv_recommendedList_playList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPlaylist.setLayoutManager(linearLayoutManager);
        mRvPlaylist.setAdapter(mPlaylistAdapter);
    }

    void initNewSong() {
        RecyclerView mRvSong = findViewById(R.id.rv_recommendedList_newSongList);
        mRvSong.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvSong.setHasFixedSize(true);
        mRvSong.setAdapter(mNewSongAdapter);
    }

    @Override
    protected int onCreateView() {
        return R.layout.recommended_list;
    }

    public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {

        @NonNull
        @Override
        public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2) {
            return new PlaylistViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.playlist_item, p1, false));
        }

        @Override
        public void onBindViewHolder(PlaylistViewHolder p1, final int p2) {
            p1.title.setText(mList.get(p2).name);
            ImageLoader.getInstance().load(mList.get(p2).picUrl).into(p1.cover);
            p1.itemView.setOnClickListener(p11 -> Navigation.startFragment(RecommendedListFragment.this, R.id.nav_browser_view, new PlaylistInfoFragment(mList.get(p2).id)));
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private final ArrayList<PlaylistModel> mList;

        public PlaylistAdapter(ArrayList<PlaylistModel> list) {
            mList = list;
        }

    }

    public class NewSongAdapter extends RecyclerView.Adapter<SongViewHolder> {

        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2) {
            return new SongViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.song_item, p1, false));
        }

        @Override
        public void onBindViewHolder(SongViewHolder p1, final int p2) {
            p1.songName.setText(mList.get(p2).name);
            p1.singerName.setText(mList.get(p2).artistsName);
            ImageLoader.getInstance().load(mList.get(p2).picUrl).into(p1.cover);
            p1.itemView.setOnClickListener(p11 -> {
                ArrayList<MediaMetadata> playList = new ArrayList<>();
                for (NewSongModel nsm : mList) {
                    playList.add(nsm.build());
                }
                PlayManager.getInstance(getActivity()).playMedia(playList, p2);
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private final ArrayList<NewSongModel> mList;

        public NewSongAdapter(ArrayList<NewSongModel> list) {
            mList = list;
        }

    }

}
