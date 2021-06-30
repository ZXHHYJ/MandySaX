package studio.mandysa.music.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import studio.mandysa.music.service.PlayManager;
import mandysax.navigation.widget.NavigationPage;
import studio.mandysa.music.R;
import studio.mandysa.music.ui.base.BaseActivity;
import studio.mandysa.music.ui.fragment.ControllerFragment;
import studio.mandysa.music.ui.fragment.PlayFragment;
import studio.mandysa.music.ui.view.bottomnavigationbar.BottomNavigationBar;
import studio.mandysa.music.ui.viewmodel.MainViewModel;

public class MainActivity extends BaseActivity {

    private FrameLayout mNavLayout;

    private SlidingUpPanelLayout mSlidingLayout;

    private ControllerFragment mControllerFragment;

    private PlayFragment mPlayFragment;

    private NavigationPage mNavigationPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MainViewModel viewModel = getViewModel(MainViewModel.class);

        mControllerFragment = getFragmentPlusManager().findFragmentById(R.id.controller_fragment);
        mPlayFragment = getFragmentPlusManager().findFragmentById(R.id.play_fragment);

        mSlidingLayout = findViewById(R.id.sl_main_view);
        mNavLayout = findViewById(R.id.fl_main_nav_layout);
        mNavigationPage = findViewById(R.id.navPage_main_layout);

        BottomNavigationBar mBottomNavigationBar = findViewById(R.id.bnb_main_view);
        mBottomNavigationBar
                .addItem(R.drawable.ic_explore, R.string.browser)
                .addItem(R.drawable.ic_cards_heart, R.string.database)
                .addItem(R.drawable.ic_search, R.string.search)
                .addItem(R.drawable.ic_account_circle, R.string.me);

        mBottomNavigationBar.setOnItemViewSelectedListener(position -> {
            mNavigationPage.showNavigation(position);
            viewModel.position = position;
        });

        if (mNavigationPage.getPosition() != viewModel.position) {
            mNavigationPage.showNavigation(viewModel.position);
            mBottomNavigationBar.setPosition(viewModel.position);
        }

        mNavigationPage.getRootView().setOnApplyWindowInsetsListener((p1, p2) -> {
            mNavigationPage.setPadding(0, p2.getSystemWindowInsetTop(), 0, 0);
            mNavLayout.setPadding(0, 0, 0, p2.getSystemWindowInsetBottom());
            return p2;
        });

        mBottomNavigationBar.post(() -> {
            mSlidingLayout.setPanelHeight(mNavLayout.getHeight() * 2 - mNavLayout.getPaddingBottom());

            final float y = mNavLayout.getY();

            if (mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                mNavLayout.setY(y + mNavLayout.getHeight());
            }

            mSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

                @Override
                public void onPanelSlide(View panel, float slideOffset) {
                    float by = y + mNavLayout.getHeight() * slideOffset * 8;
                    mNavLayout.setY(by);
                    float alpha = slideOffset * 12;
                    mControllerFragment.getRoot().setAlpha(1 - alpha);
                    mPlayFragment.getRoot().setAlpha(alpha);
                }

                @Override
                public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                }

            });
        });

        initPmi();
    }

    private void initPmi() {
        if (PlayManager.getInstance(this).getMediaMetadataLiveData().getValue() == null) {
            mSlidingLayout.setTouchEnabled(false);
            PlayManager.getInstance(this).getMediaMetadataLiveData().lazy(this, p1 -> mSlidingLayout.setTouchEnabled(p1 != null));
        }
    }

    @Override
    public void onBackPressed() {
        if (mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        if (!mNavigationPage.onBackPressed())
            super.onBackPressed();
    }

}
