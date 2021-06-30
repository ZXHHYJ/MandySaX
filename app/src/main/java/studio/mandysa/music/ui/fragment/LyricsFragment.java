package studio.mandysa.music.ui.fragment;

import android.media.MediaMetadata;
import android.os.Bundle;
import android.view.View;

import mandysax.anna2.callback.Callback;
import mandysax.lifecycle.livedata.Observer;
import studio.mandysa.music.service.Media;
import studio.mandysa.music.service.PlayManager;
import me.wcy.lrcview.LrcView;
import studio.mandysa.music.R;
import studio.mandysa.music.logic.Model.LyricModel;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Network.ServiceCreator;
import studio.mandysa.music.ui.base.BaseFragment;

public class LyricsFragment extends BaseFragment {

    @Override
    protected void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        LrcView lrcView = findViewById(R.id.lrcView_lyrics_view);
        PlayFragment playFragment = getFragmentPlusManager().findFragmentById(R.id.play_fragment);
        lrcView.setOnTapListener((view1, x, y) -> playFragment.showMusicFragment());
        PlayManager.getInstance(this).getMediaMetadataLiveData().observeForever(new Observer<MediaMetadata>() {
            @Override
            public void onChanged(MediaMetadata p1) {
                if (p1 != null) {
                    System.out.println(p1.getString(Media.MEDIA_ID));
                    ServiceCreator.create(NeteaseCloudMusicApi.class).getLyric(p1.getString(Media.MEDIA_ID)).set(new Callback<LyricModel>() {
                        @Override
                        public void onResponse(boolean loaded, LyricModel lyricModel) {
                            lrcView.loadLrc(lyricModel.Lyric);
                        }

                        @Override
                        public void onFailure(int code) {
                        }
                    });
                }
            }
        });
    }

    @Override
    protected int onCreateView() {
        return R.layout.lyrics;
    }

}
