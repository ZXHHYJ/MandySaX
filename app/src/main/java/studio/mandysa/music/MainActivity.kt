package studio.mandysa.music

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.yanzhenjie.sofia.Sofia
import mandysax.fragment.Fragment
import mandysax.fragmentpage.widget.FragmentPage
import mandysax.media.DefaultPlayerManager
import mandysax.navigation.fragment.NavHostFragment
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.ui.base.BaseActivity
import studio.mandysa.music.ui.event.ShareViewModel
import studio.mandysa.music.ui.home.HomeFragment
import studio.mandysa.music.ui.search.SearchFragment

class MainActivity : BaseActivity() {

    private val mBinding: ActivityMainBinding by inflate()

    private val mEvent: ShareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        Sofia.with(this).invasionStatusBar().invasionNavigationBar().statusBarDarkFont()
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.apply {
            mainSlidingView.shadowHeight = 0
            mainFragmentPage.setAdapter(object : FragmentPage.Adapter {
                override fun onCreateFragment(position: Int): Fragment? =
                    when (position) {
                        0 -> NavHostFragment.create(HomeFragment())
                        1 -> NavHostFragment.create(SearchFragment())
                        else -> null
                    }
            })
            mEvent.homePosLiveData.observe(this@MainActivity,
                { mainFragmentPage.position = it })
            bottomNavigationBar.addItem(R.drawable.ic_home, getString(R.string.title_home))
            bottomNavigationBar.addItem(R.drawable.ic_search, getString(R.string.title_search))
            bottomNavigationBar.setOnItemViewSelectedListener {
                when (it) {
                    0 -> {
                        mEvent.homePosLiveData.value = 0
                    }
                    1 -> {
                        mEvent.homePosLiveData.value = 1
                    }
                }
            }
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                if (mainSlidingView.panelHeight == 0) {
                    bottomNavigationBar.post {
                        mainSlidingView.panelHeight =
                            bottomNavigationBar.height
                        controllerFragment.layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            bottomNavigationBar.height
                        )
                    }
                }
                mainFragmentPage.setPadding(
                    0,
                    insets.systemWindowInsetTop,
                    0,
                    insets.systemWindowInsetBottom
                )
                bottomNavLayout.setPadding(
                    insets.systemWindowInsetLeft,
                    0,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
                //initPmi()
                insets
            }
        }
    }

    private fun initPmi() {
        val instance = DefaultPlayerManager.getInstance().changeMusicLiveData()
        instance.observe(this) {
            instance.removeObservers(this)
            mBinding.run {
                bottomNavLayout.post {
                    mainSlidingView.run {
                        panelHeight =
                            mBinding.bottomNavLayout.height * 2 - mBinding.bottomNavLayout.paddingBottom
                        shadowHeight =
                            resources.getDimensionPixelSize(R.dimen.umano_shadow_height)
                        postDelayed({
                            addPanelSlideListener(object :
                                SlidingUpPanelLayout.PanelSlideListener {
                                val y: Float = mBinding.bottomNavLayout.y
                                override fun onPanelSlide(panel: View, slideOffset: Float) {
                                    mBinding.run {
                                        val by: Float = y + bottomNavLayout.height * slideOffset * 8
                                        bottomNavLayout.y = by
                                        val alpha = slideOffset * 12
                                        controllerFragment.alpha = 1 - alpha
                                        playFragment.alpha = alpha
                                    }
                                }

                                override fun onPanelStateChanged(
                                    panel: View,
                                    previousState: SlidingUpPanelLayout.PanelState,
                                    newState: SlidingUpPanelLayout.PanelState
                                ) {
                                }
                            })
                        }, 220)
                    }
                }
            }
        }
    }

}