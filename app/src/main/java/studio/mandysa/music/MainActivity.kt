package studio.mandysa.music

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.yanzhenjie.sofia.Sofia
import mandysax.fragment.Fragment
import mandysax.fragment.FragmentActivity
import mandysax.fragment.FragmentView
import mandysax.fragmentpage.widget.FragmentPage
import mandysax.lifecycle.Lifecycle
import mandysax.media.DefaultPlayerManager
import mandysax.navigation.fragment.NavHostFragment
import studio.mandysa.bottomnavigationbar.BottomNavigationItem
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.logic.utils.inflate
import studio.mandysa.music.logic.utils.viewModels
import studio.mandysa.music.ui.event.ShareViewModel
import studio.mandysa.music.ui.home.HomeFragment
import studio.mandysa.music.ui.me.MeFragment
import studio.mandysa.music.ui.search.SearchFragment


class MainActivity : FragmentActivity() {

    private val mBinding: ActivityMainBinding by inflate()

    private val mEvent: ShareViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        Sofia.with(this).invasionStatusBar().invasionNavigationBar().statusBarDarkFont()
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val controllerFragment = findViewById<FragmentView>(R.id.controller_fragment)
        val playFragment = findViewById<FragmentView>(R.id.play_fragment)
        //解决playFragment点击无事件view会关闭播放界面的问题
        playFragment.setOnTouchListener { _, _ -> mBinding.mainSlidingView.panelState == SlidingUpPanelLayout.PanelState.EXPANDED }
        lifecycle.addObserver { state ->
            //避免内存泄漏
            if (state == Lifecycle.Event.ON_DESTROY)
                playFragment.setOnTouchListener(null)
        }
        mBinding.apply {
            //不把shadowHeight设置为0的话后续修改shadowHeight都将失效！！
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
            bottomNavigationBar.setActiveColorResource(R.color.main)
            bottomNavigationBar.models = listOf(
                BottomNavigationItem(
                    R.drawable.ic_home,
                    getString(R.string.home)
                ),
                BottomNavigationItem(
                    R.drawable.ic_search,
                    getString(R.string.search)
                ), BottomNavigationItem(
                    R.drawable.ic_person,
                    getString(R.string.me)
                )
            )
            if (bottomNavigationBar.getSelectedPosition().value == -1)
                bottomNavigationBar.setSelectedPosition(0)
            if (bottomNavigationBar.getSelectedPosition().value != mEvent.homePosLiveData.value)
                bottomNavigationBar.setSelectedPosition(mEvent.homePosLiveData.value)
            mEvent.homePosLiveData.observe(this@MainActivity,
                {
                    mainFragmentPage.position = it
                })
            bottomNavigationBar.getSelectedPosition().observe(this@MainActivity) {
                mEvent.homePosLiveData.value = it
            }
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                //避免底部导航遮挡内容
                if (mainSlidingView.panelHeight == 0 && bottomNavLayout != null) {
                    bottomNavigationBar.post {
                        mainSlidingView.panelHeight =
                            resources.getDimensionPixelOffset(R.dimen.nav_height)
                    }
                }
                mainFragmentPage.setPadding(
                    0,
                    insets.systemWindowInsetTop,
                    0,
                    insets.systemWindowInsetBottom
                )
                bottomNavLayout?.setPadding(
                    insets.systemWindowInsetLeft,
                    0,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
                DefaultPlayerManager.getInstance()!!.changeMusicLiveData().lazy(this@MainActivity) {
                    mBinding.apply {
                        bottomNavigationBar.post {
                            mainSlidingView.apply {
                                panelHeight =
                                    (context.resources.getDimensionPixelOffset(R.dimen.nav_height) * if (bottomNavLayout != null) 2 else 1) + mBinding.mainFragmentPage.paddingBottom
                                mBinding.mainFragmentPage.setPadding(
                                    0,
                                    mBinding.mainFragmentPage.paddingTop,
                                    0,
                                    0
                                )
                                shadowHeight =
                                    resources.getDimensionPixelSize(R.dimen.umano_shadow_height)
                                postDelayed({
                                    addPanelSlideListener(object :
                                        SlidingUpPanelLayout.PanelSlideListener {
                                        val y = mBinding.bottomNavLayout?.y
                                        override fun onPanelSlide(panel: View, slideOffset: Float) {
                                            mBinding.apply {
                                                y?.apply {
                                                    val by: Float =
                                                        y + bottomNavLayout!!.height * slideOffset * 8
                                                    bottomNavLayout.y = by
                                                }
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
                insets
            }
        }
    }

    override fun onBackPressed() {
        if (mBinding.mainSlidingView.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mBinding.mainSlidingView.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else super.onBackPressed()
    }

}