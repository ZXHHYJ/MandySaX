package studio.mandysa.music

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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
import studio.mandysa.music.ui.me.MeFragment
import studio.mandysa.music.ui.search.SearchFragment

class MainActivity : BaseActivity() {

    private val mBinding: ActivityMainBinding by inflate()

    private val mEvent: ShareViewModel by viewModels()

    private var controllerFragment: ViewGroup? = null

    private var playFragment: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Sofia.with(this).invasionStatusBar().invasionNavigationBar().statusBarDarkFont()
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        controllerFragment = findViewById(R.id.controller_fragment)
        playFragment = findViewById(R.id.play_fragment)
        mBinding.apply {
            mainSlidingView.shadowHeight = 0
            mainFragmentPage.setAdapter(object : FragmentPage.Adapter {
                override fun onCreateFragment(position: Int): Fragment =
                    NavHostFragment.create(
                        when (position) {
                            0 -> HomeFragment()
                            1 -> SearchFragment()
                            2 -> MeFragment()
                            else -> null
                        }
                    )
            })
            mEvent.homePosLiveData.observe(this@MainActivity,
                { mainFragmentPage.position = it })
            bottomNavigationBar.addItem(R.drawable.ic_home, getString(R.string.title_home))
            bottomNavigationBar.addItem(R.drawable.ic_search, getString(R.string.title_search))
            bottomNavigationBar.addItem(R.drawable.ic_person, getString(R.string.me))
            bottomNavigationBar.setTextColor(R.color.main)
            bottomNavigationBar.setOnItemViewSelectedListener {
                mEvent.homePosLiveData.value = it
            }
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                if (mainSlidingView.panelHeight == 0) {
                    bottomNavigationBar.post {
                        mainSlidingView.panelHeight =
                            bottomNavigationBar.height
                        controllerFragment!!.layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            bottomNavigationBar.height
                        )
                    }
                }
                mainFragmentPage.setPadding(
                    0,
                    insets.systemWindowInsetTop,
                    0,
                    0
                )
                bottomNavLayout.setPadding(
                    insets.systemWindowInsetLeft,
                    0,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
                initPmi()
                insets
            }
        }
    }

    private fun initPmi() {
        val instance = DefaultPlayerManager.getInstance()!!.changeMusicLiveData()
        instance.lazy(this) {
            mBinding.run {
                bottomNavigationBar.post {
                    mainSlidingView.run {
                        panelHeight =
                            mBinding.bottomNavigationBar.height * 2 + mBinding.bottomNavigationBar.paddingBottom
                        shadowHeight =
                            resources.getDimensionPixelSize(R.dimen.umano_shadow_height)
                        postDelayed({
                            addPanelSlideListener(object :
                                SlidingUpPanelLayout.PanelSlideListener {
                                val y: Float = mBinding.bottomNavLayout.y
                                override fun onPanelSlide(panel: View, slideOffset: Float) {
                                    mBinding.run {
                                        val by: Float =
                                            y + bottomNavLayout.height * slideOffset * 8
                                        bottomNavLayout.y = by
                                        val alpha = slideOffset * 12
                                        controllerFragment!!.alpha = 1 - alpha
                                        playFragment!!.alpha = alpha
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

    /*override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            ImageLoader.getInstance().destroy()
        }
    }*/

}