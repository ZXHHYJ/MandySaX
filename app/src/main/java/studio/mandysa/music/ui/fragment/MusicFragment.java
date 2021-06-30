package studio.mandysa.music.ui.fragment;

import android.annotation.SuppressLint;
import android.media.MediaMetadata;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import studio.mandysa.music.service.PlayManager;
import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BasePlayFragment;
import studio.mandysa.music.utils.ImageLoader;

public class MusicFragment extends BasePlayFragment {

    @Override
    public void setPlayModel() {
        mPlayOrPause.setImageResource(R.mipmap.ic_play);
    }

    @Override
    public void setPauseModel() {
        mPlayOrPause.setImageResource(R.mipmap.ic_pause);
    }

    private TextView mSongName, mSingerName;

    private ImageView mCover;
    private ImageView mPlayOrPause;


    @Override
    protected void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSongName = findViewById(R.id.tv_music_songName);
        mSingerName = findViewById(R.id.tv_music_singerName);
        mCover = findViewById(R.id.ic_music_cover);
        SeekBar mPlayProgress = findViewById(R.id.skBar_music_progress);
        ImageView mSkipPrevious = findViewById(R.id.iv_music_skipPrevious);
        mPlayOrPause = findViewById(R.id.iv_music_playOrPause);
        ImageView mSkipNext = findViewById(R.id.iv_music_skipNext);
        mSkipPrevious.setImageResource(R.mipmap.ic_skip_previous);
        mSkipNext.setImageResource(R.mipmap.ic_skip_next);

        PlayFragment playFragment = getFragmentPlusManager().findFragmentById(R.id.play_fragment);
        mCover.setOnClickListener(v -> playFragment.showLyricsFragment());

        mSkipPrevious.setOnClickListener(p1 -> PlayManager.getInstance(getActivity()).skipToPrevious());
        mPlayOrPause.setOnClickListener(p1 -> {
            if (getIsPlaying()) {
                PlayManager.getInstance(getActivity()).pause();
            } else {
                PlayManager.getInstance(getActivity()).play();
            }
        });
        mSkipNext.setOnClickListener(p1 -> PlayManager.getInstance(getActivity()).skipToNext());
    }

    @Override
    protected int onCreateView() {
        return R.layout.music;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PlayManager.getInstance(this).getMediaMetadataLiveData().observe(this, p1 -> {
            if (p1 == null) return;
            mSongName.setText(p1.getString(MediaMetadata.METADATA_KEY_TITLE));
            mSingerName.setText(p1.getString(MediaMetadata.METADATA_KEY_ARTIST));
        });
        getShareViewModel().picUrl.observe(this, p1 -> ImageLoader.getInstance().load(p1).into(mCover));
    }

    /*private void onGenerated(Palette palette) {
        assert palette != null;
        Palette.Swatch vibrant;
        vibrant = palette.getLightMutedSwatch();
        if (vibrant == null) {
            vibrant = palette.getMutedSwatch();
        }
        if (vibrant == null) {
            vibrant = palette.getDarkMutedSwatch();
        }
        if (vibrant == null) {
            vibrant = palette.getDarkVibrantSwatch();
        }
        if (vibrant != null) {
            mSongName.setTextColor(vibrant.getBodyTextColor());
            mSingerName.setTextColor(vibrant.getTitleTextColor());

            getRoot().setBackgroundColor(vibrant.getRgb());
        }
        vibrant = palette.getDarkMutedSwatch();
        if (vibrant == null) return;
        mSkipPrevious.setColorFilter(vibrant.getRgb());
        mPlayOrPause.setColorFilter(vibrant.getRgb());
        mSkipNext.setColorFilter(vibrant.getRgb());
    }*/

}
