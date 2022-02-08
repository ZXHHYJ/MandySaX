package studio.mandysa.music

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sothree.slidinguppanel.PanelSlideListener
import com.sothree.slidinguppanel.PanelState
import com.yanzhenjie.sofia.Sofia
import mandysax.fragment.FragmentActivity
import mandysax.tablayout.BottomNavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.logic.utils.getInstance
import studio.mandysa.music.logic.utils.inflate
import studio.mandysa.music.logic.utils.viewModels


class MainActivity : FragmentActivity() {

    private val mBinding: ActivityMainBinding by inflate()

    private val mViewModel: MainViewModel by viewModels()

    //private val mUserViewModel: UserViewModel by viewModels()

    private fun isNightMode() =
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        Sofia.with(this@MainActivity).let {
            it.invasionStatusBar().invasionNavigationBar()
            if (isNightMode())
                it.statusBarLightFont().navigationBarLightFont()
            else it.statusBarDarkFont().navigationBarDarkFont()
        }
        mBinding.apply {
            //不把shadowHeight设置为0的话后续修改shadowHeight都将失效！！
            mainSlidingView.shadowHeight = 0
            //解决playFragment点击无事件view会关闭播放界面的问题
            controllerFragment.setOnClickListener {
                if (getInstance().changeMusicLiveData().value != null)
                    mBinding.mainSlidingView.panelState = PanelState.EXPANDED
            }
            mainViewPager.setUserInputEnabled(false)
            mainViewPager.adapter = mViewModel.adapter
            mainBottomNavigationBar.models = listOf(
                BottomNavigationItem(
                    R.drawable.ic_round_contactless_24,
                    getString(R.string.home)
                ),
                BottomNavigationItem(
                    R.drawable.ic_round_account_circle_24,
                    getString(R.string.me)
                )
            )
                .setActiveColor(getColor(R.color.theme_color))
                .setInActiveColor(getColor(mandysax.tablayout.R.color.default_unchecked_color))
            mainBottomNavigationBar.setSelectedPosition(mainViewPager.currentItem)
            mainBottomNavigationBar.getSelectedPosition().observe(this@MainActivity) {
                mainViewPager.currentItem = it
            }
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                val startBarSize = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                val navigationBarSize =
                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                val music = getInstance().changeMusicLiveData().value != null
                bottomNavLayout.setPadding(
                    0,
                    0,
                    0,
                    navigationBarSize
                )
                mainViewPager.setPadding(
                    0,
                    startBarSize,
                    0,
                    if (music) 0 else resources.getDimensionPixelOffset(R.dimen.nav_height) + navigationBarSize
                )
                if (music) {
                    mainSlidingView.panelHeight =
                        resources.getDimensionPixelOffset(R.dimen.nav_height) * 2 + navigationBarSize
                }
                insets
            }
            getInstance().changeMusicLiveData().observe(this@MainActivity) {
                mBinding.apply {
                    mainBottomNavigationBar.post {
                        mainSlidingView.apply {
                            panelHeight =
                                resources.getDimensionPixelOffset(R.dimen.nav_height) * 2 + bottomNavLayout.paddingBottom
                            mBinding.mainViewPager.setPadding(
                                0,
                                mBinding.mainViewPager.paddingTop,
                                0,
                                0
                            )
                            shadowHeight =
                                resources.getDimensionPixelSize(R.dimen.umano_shadow_height)
                            postDelayed({
                                val y =
                                    mBinding.root.bottom - mBinding.bottomNavLayout.height
                                addPanelSlideListener(object :
                                    PanelSlideListener {
                                    override fun onPanelSlide(panel: View, slideOffset: Float) {
                                        val by: Float =
                                            y + bottomNavLayout.height * slideOffset * 8
                                        bottomNavLayout.y = by
                                        val alpha = slideOffset * 12
                                        playFragment.alpha = alpha
                                        controllerFragment.alpha = 1 - alpha
                                    }

                                    override fun onPanelStateChanged(
                                        panel: View,
                                        previousState: PanelState,
                                        newState: PanelState
                                    ) {
                                        when (newState) {
                                            PanelState.EXPANDED -> {
                                                Sofia.with(this@MainActivity)
                                                    .statusBarLightFont()
                                            }
                                            PanelState.DRAGGING -> {

                                            }
                                            else -> {
                                                Sofia.with(this@MainActivity).let {
                                                    if (isNightMode())
                                                        it.statusBarLightFont()
                                                            .navigationBarLightFont()
                                                    else it.statusBarDarkFont()
                                                        .navigationBarDarkFont()
                                                }
                                            }
                                        }
                                    }
                                })
                            }, 220)
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        when (mBinding.mainSlidingView.panelState) {
            PanelState.EXPANDED, PanelState.DRAGGING -> {
                mBinding.mainSlidingView.panelState = PanelState.COLLAPSED
            }
            else -> super.onBackPressed()
        }
    }

}