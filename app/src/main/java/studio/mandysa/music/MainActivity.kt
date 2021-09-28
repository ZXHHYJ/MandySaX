package studio.mandysa.music

import android.os.Bundle
import com.yanzhenjie.sofia.Sofia
import studio.mandysa.music.databinding.ActivityMainBinding
import studio.mandysa.music.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private val mBinding: ActivityMainBinding by inflate()
    override fun onCreate(savedInstanceState: Bundle?) {
        Sofia.with(this).invasionStatusBar().invasionNavigationBar()
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}