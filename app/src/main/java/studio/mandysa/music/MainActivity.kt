package studio.mandysa.music

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sothree.slidinguppanel.PanelSlideListener
import com.sothree.slidinguppanel.PanelState
import com.yanzhenjie.sofia.Sofia
import mandysax.fragment.Fragment
import mandysax.fragment.FragmentActivity
import mandysax.fragment.FragmentManager
import mandysax.navigation.fragment.NavHostFragment
import mandysax.tablayout.BottomNavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import mandysax.viewpager.adapter.FragmentStateAdapter
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.logic.utils.getInstance
import studio.mandysa.music.logic.utils.inflate
import studio.mandysa.music.logic.utils.viewModels
import studio.mandysa.music.ui.event.UserViewModel
import studio.mandysa.music.ui.home.HomeFragment
import studio.mandysa.music.ui.login.LoginFragment
import studio.mandysa.music.ui.me.MeFragment


class MainActivity : FragmentActivity() {

    private val mBinding: ActivityMainBinding by inflate()

    private val mUserViewModel: UserViewModel by viewModels()

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

        //判断有没有登录，没有登录的话就打开登录界面
        if (mUserViewModel.getCookieLiveData().value == null)
            LoginFragment().show(fragmentPlusManager)
        mBinding.apply {
            //不把shadowHeight设置为0的话后续修改shadowHeight都将失效！！
            mainSlidingView.shadowHeight = 0
            //解决playFragment点击无事件view会关闭播放界面的问题
            controllerFragment.setOnClickListener {
                mBinding.mainSlidingView.panelState = PanelState.EXPANDED
            }
            mainViewPager.setUserInputEnabled(false)
            mainViewPager.adapter = object : FragmentStateAdapter() {
                private val list = listOf(
                    HomeFragment(), MeFragment()
                )

                override fun createFragment(position: Int): Fragment {
                    return NavHostFragment.create(list[position])
                }

                override fun getItemCount(): Int {
                    return list.size
                }

                override fun getFragmentManager(): FragmentManager {
                    return fragmentPlusManager
                }

            }
            bottomNavigationBar.models = listOf(
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
                .setInActiveColor(getColor(R.color.default_unchecked_color))
            bottomNavigationBar.setSelectedPosition(mainViewPager.currentItem)
            bottomNavigationBar.getSelectedPosition().observe(this@MainActivity) {
                mainViewPager.currentItem = it
            }
            mainSlidingView.isTouchEnabled = false
            ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
                //避免底部导航遮挡内容
                if (mainSlidingView.panelHeight == 0) {
                    mainSlidingView.panelHeight =
                        resources.getDimensionPixelOffset(R.dimen.nav_height)
                }
                val startBarSize = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                val navigationBarSize =
                    insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                val defaultPlayManager = getInstance()
                mainViewPager.setPadding(
                    0,
                    startBarSize,
                    0,
                    if (defaultPlayManager.changeMusicLiveData().value == null) navigationBarSize else 0
                )
                bottomNavLayout.setPadding(
                    0,
                    0,
                    0,
                    navigationBarSize
                )
                defaultPlayManager.changeMusicLiveData().lazy(this@MainActivity) {
                    mBinding.apply {
                        if (mainSlidingView.isTouchEnabled)
                            return@lazy
                        mainSlidingView.isTouchEnabled = true
                        bottomNavigationBar.post {
                            mainSlidingView.apply {
                                panelHeight =
                                    (context.resources.getDimensionPixelOffset(R.dimen.nav_height) * 2) + navigationBarSize
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
                insets
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