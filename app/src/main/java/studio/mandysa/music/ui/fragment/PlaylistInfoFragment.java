package studio.mandysa.music.ui.fragment;

import android.media.MediaMetadata;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mandysax.anna2.callback.Callback;
import studio.mandysa.music.service.PlayManager;
import studio.mandysa.music.R;
import studio.mandysa.music.logic.Model.MusicModel;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Model.PlaylistInfoModel;
import studio.mandysa.music.logic.Network.ServiceCreator;
import studio.mandysa.music.ui.base.BaseLoadingFragment;
import studio.mandysa.music.ui.viewholder.SongViewHolder;
import studio.mandysa.music.utils.ImageLoader;

public class PlaylistInfoFragment extends BaseLoadingFragment {

    private TextView mPlaylistName, mPlaylistDescription;

    private ImageView mPlaylistCover;

    private final ArrayList<PlaylistInfoModel.SongList> mSongList = new ArrayList<>();

    private final ArrayList<MusicModel> mMusicModelList = new ArrayList<>();

    private SongListAdapter mSongListAdapter;

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRvSongList = findViewById(R.id.rv_playlistInfo_songList);
        mRvSongList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvSongList.setHasFixedSize(true);

        mRvSongList.setAdapter(mSongListAdapter = new SongListAdapter(mMusicModelList));

        mPlaylistName = findViewById(R.id.tv_playlist_name);
        mPlaylistDescription = findViewById(R.id.tv_playlist_description);
        mPlaylistCover = findViewById(R.id.iv_playlist_cover);
        initPlaylists();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        initPlaylists();
    }

    private void initPlaylists() {
        ServiceCreator.create(NeteaseCloudMusicApi.class).getSongListInfo(mId).set(new Callback<PlaylistInfoModel>() {

            @Override
            public void onResponse(boolean loaded, PlaylistInfoModel t) {
                mPlaylistName.setText(t.name);
                if (!t.description.equals("null"))
                    mPlaylistDescription.setText(t.description);
                ImageLoader.getInstance().load(t.coverImgUrl).into(mPlaylistCover);
                mSongList.addAll(t.songList);
                initSongList();
            }

            @Override
            public void onFailure(int code) {
                show();
            }
        });
    }

    private void initSongList() {
        List<String> idList = new ArrayList<>();
        for (PlaylistInfoModel.SongList song : mSongList) {
            idList.add(song.id);
        }
        ServiceCreator.create(NeteaseCloudMusicApi.class).getMusicInfo(idList).set(new Callback<MusicModel>() {
            @Override
            public void onResponse(boolean loaded, MusicModel t) {
                mMusicModelList.add(t);
                if (loaded) {
                    mSongListAdapter.notifyDataSetChanged();
                }
                hide();
            }

            @Override
            public void onFailure(int code) {
                show();
            }
        });
    }

    @Override
    protected int onCreateView() {
        return R.layout.playlist_info;
    }

    private final String mId;

    public PlaylistInfoFragment(String id) {
        mId = id;
    }

    public class SongListAdapter extends RecyclerView.Adapter<SongViewHolder> {

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
                for (MusicModel mm : mList) {
                    playList.add(mm.build());
                }
                PlayManager.getInstance(getActivity()).playMedia(playList, p2);
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private final ArrayList<MusicModel> mList;

        public SongListAdapter(ArrayList<MusicModel> list) {
            mList = list;
        }

    }

}
