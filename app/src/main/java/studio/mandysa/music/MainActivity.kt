package studio.mandysa.music

import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.yanzhenjie.sofia.Sofia
import mandysax.fragment.Fragment
import mandysax.fragmentpage.widget.FragmentPage
import mandysax.navigation.fragment.NavHostFragment
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.ui.base.BaseActivity
import studio.mandysa.music.ui.home.HomeFragment
import studio.mandysa.music.ui.search.SearchFragment

class MainActivity : BaseActivity() {
    private val mBinding: ActivityMainBinding by inflate()
    override fun onCreate(savedInstanceState: Bundle?) {
        Sofia.with(this).invasionStatusBar().invasionNavigationBar()
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
            bottomNavigationBar.addItem(R.drawable.ic_home, getString(R.string.title_home))
            bottomNavigationBar.addItem(R.drawable.ic_search, getString(R.string.title_search))
            bottomNavigationBar.setOnItemViewSelectedListener {
                when (it) {
                    0 -> {
                        mainFragmentPage.position = 0
                    }
                    1 -> {
                        mainFragmentPage.position = 1
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

}