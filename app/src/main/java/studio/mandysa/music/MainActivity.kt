package studio.mandysa.music

import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.yanzhenjie.sofia.Sofia
import mandysax.navigation.Navigation
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.ui.base.BaseActivity
import studio.mandysa.music.ui.search.SearchFragment

class MainActivity : BaseActivity() {
    private val mBinding: ActivityMainBinding by inflate()
    override fun onCreate(savedInstanceState: Bundle?) {
        Sofia.with(this).invasionStatusBar().invasionNavigationBar()
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.apply {
            mainSlidingView.shadowHeight = 0
            bottomNavigationBar.addItem(R.drawable.ic_home, getString(R.string.title_home))
            bottomNavigationBar.addItem(R.drawable.ic_search, getString(R.string.title_search))
            bottomNavigationBar.setOnItemViewSelectedListener {
                when (it) {
                    0 -> {
                        Navigation.findViewNavController(mainFragment).navigateUp()
                    }
                    1 -> {
                        Navigation.findViewNavController(mainFragment).navigate(SearchFragment())
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
                mainFragment.setPadding(
                    0,
                    insets.systemWindowInsetTop,
                    0,
                    0
                )
                bottomNavigationBar.setPadding(
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

    override fun onBackPressed() {
        if (mBinding.bottomNavigationBar.position == 1) {
            mBinding.bottomNavigationBar.position = 0
        } else
            super.onBackPressed()
    }
}