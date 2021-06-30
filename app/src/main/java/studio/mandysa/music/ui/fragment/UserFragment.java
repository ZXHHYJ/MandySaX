package studio.mandysa.music.ui.fragment;

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

import mandysax.anna2.callback.Callback;
import mandysax.navigation.widget.Navigation;
import studio.mandysa.music.R;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Model.UserModel;
import studio.mandysa.music.logic.Model.UserPlaylistModel;
import studio.mandysa.music.logic.Network.ServiceCreator;
import studio.mandysa.music.ui.base.BaseLoadingFragment;
import studio.mandysa.music.ui.viewholder.SongViewHolder;
import studio.mandysa.music.ui.viewmodel.LoginViewModel;
import studio.mandysa.music.ui.viewmodel.UserViewModel;
import studio.mandysa.music.utils.ImageLoader;

public class UserFragment extends BaseLoadingFragment {

    private LoginViewModel mLoginViewModel;

    private UserViewModel mUserViewModel;

    private ImageView mAvatar;

    private TextView mName, mDescription;

    private final ArrayList<UserPlaylistModel> mList = new ArrayList<>();

    private final UserPlaylistAdapter mAdapter = new UserPlaylistAdapter(mList);

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLoginViewModel = getViewModel(LoginViewModel.class);
        mUserViewModel = getViewModel(UserViewModel.class);

        mName = findViewById(R.id.tv_user_name);
        mDescription = findViewById(R.id.tv_user_description);
        mAvatar = findViewById(R.id.iv_user_avatar);

        RecyclerView mUserPlaylistList = findViewById(R.id.rv_user_playlist);
        mUserPlaylistList.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserPlaylistList.setHasFixedSize(true);
        mUserPlaylistList.setAdapter(mAdapter);

        mLoginViewModel.getCookieLiveData().lazy(p1 -> {
            if (p1 != null)
                initUserInfoRefresh();
        });

    }

    private void initUserInfoRefresh() {
        mLoginViewModel.getCookieLiveData().lazy(p1 -> mUserViewModel.getUserModel(p1).set(new Callback<UserModel>() {

            @Override
            public void onResponse(boolean loaded, UserModel t) {
                ImageLoader.getInstance().load(t.avatarUrl).into(mAvatar);
                mName.setText(t.nickname);
                mDescription.setText(t.signature);
                initUserPlaylistRefresh(t.userId);
                hide();
            }

            @Override
            public void onFailure(int code) {
                show();
            }

        }));

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        initUserInfoRefresh();
    }

    private void initUserPlaylistRefresh(String userid) {
        ServiceCreator.create(NeteaseCloudMusicApi.class).getUserPlaylist(userid).set(new Callback<UserPlaylistModel>() {

            @Override
            public void onResponse(boolean loaded, UserPlaylistModel t) {
                mList.add(t);
                if (loaded) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int code) {
                //  show();
            }

        });
    }

    @Override
    protected int onCreateView() {
        return R.layout.user;
    }

    public class UserPlaylistAdapter extends RecyclerView.Adapter<SongViewHolder> {

        @NonNull
        @Override
        public SongViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2) {
            return new SongViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.song_item, p1, false));
        }

        @Override
        public void onBindViewHolder(SongViewHolder p1, final int p2) {
            p1.songName.setText(mList.get(p2).name);
            p1.singerName.setText(mList.get(p2).nickname);
            ImageLoader.getInstance().load(mList.get(p2).coverImgUrl).into(p1.cover);
            p1.itemView.setOnClickListener(p11 -> Navigation.startFragment(UserFragment.this, R.id.nav_me_view, new PlaylistInfoFragment(mList.get(p2).id)));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        private final ArrayList<UserPlaylistModel> mList;

        public UserPlaylistAdapter(ArrayList<UserPlaylistModel> list) {
            mList = list;
        }

    }

}
