package studio.mandysa.music.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import studio.mandysa.music.service.Media;
import studio.mandysa.music.service.PlayManager;
import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BasePlayFragment;
import studio.mandysa.music.utils.ImageLoader;

public class ControllerFragment extends BasePlayFragment {

    private TextView mSongName;

    private ImageView mCover;
    private ImageView mPlayOrPause;

    void setTitle(String title) {
        mSongName.setText(title);
    }

    @Override
    public void setPlayModel() {
        mPlayOrPause.setImageResource(R.mipmap.ic_play);
    }

    @Override
    public void setPauseModel() {
        mPlayOrPause.setImageResource(R.mipmap.ic_pause);
    }

    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSongName = findViewById(R.id.tv_controller_songName);
        mCover = findViewById(R.id.iv_controller_cover);
        mPlayOrPause = findViewById(R.id.iv_controller_playOrPause);

        setPlayModel();

        ImageView mNextSong = findViewById(R.id.iv_controller_nextSong);

        mNextSong.setImageResource(R.mipmap.ic_skip_next);

        mPlayOrPause.setOnClickListener(p1 -> {
            if (getIsPlaying()) {
                PlayManager.getInstance(ControllerFragment.this).pause();
            } else {
                PlayManager.getInstance(ControllerFragment.this).play();
            }
        });
        mNextSong.setOnClickListener(p1 -> PlayManager.getInstance(ControllerFragment.this).skipToNext());

    }

    @Override
    protected int onCreateView() {
        return R.layout.controller;
    }

    @Override
    protected void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PlayManager.getInstance(this).getMediaMetadataLiveData().observe(this, p1 -> {
            if (p1 == null) return;
            setTitle(p1.getString(Media.TITLE));
            //mMusicView.setSubTitle(p1.getString(MediaMetadata.METADATA_KEY_ARTIST));
        });
        getShareViewModel().picUrl.observe(this, p1 -> ImageLoader.getInstance().load(p1).into(mCover));
    }

}
