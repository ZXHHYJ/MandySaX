package studio.mandysa.music.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseFragment;

public class PlayFragment extends BaseFragment {

    private final MusicFragment mMusicFragment = new MusicFragment();

    private final LyricsFragment mLyricsFragment = new LyricsFragment();

    @Override
    protected void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        getFragmentPlusManager().add(getRoot().getId(), mMusicFragment).add(getRoot().getId(), mLyricsFragment).hide(mLyricsFragment).commitNow();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SlidingUpPanelLayout slidingUpPanelLayout = getActivity().findViewById(R.id.sl_main_view);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    if (mMusicFragment.isHidden())
                        showMusicFragment();
                }
            }
        });
        getRoot().setOnTouchListener((p1, p2) -> slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void showMusicFragment() {
        getFragmentPlusManager().setCustomAnimations(R.anim.anim_fade_in, 0, 0, 0).show(mMusicFragment).hide(mLyricsFragment).commitNow();
    }

    public void showLyricsFragment() {
        getFragmentPlusManager().setCustomAnimations(R.anim.anim_fade_in, 0, 0, 0).show(mLyricsFragment).hide(mMusicFragment).commitNow();
    }

    @Override
    protected int onCreateView() {
        return R.layout.play;
    }

}
