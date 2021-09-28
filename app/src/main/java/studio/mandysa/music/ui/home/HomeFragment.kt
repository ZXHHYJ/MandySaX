package studio.mandysa.music.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.mandysa.music.databinding.FragmentHomeBinding
import studio.mandysa.jiowo.jiuwo.utils.RecyclerViewUtils.linear
import studio.mandysa.jiowo.jiuwo.utils.RecyclerViewUtils.setup
import studio.mandysa.music.ui.base.BaseFragment

class HomeFragment : BaseFragment() {

    private val mBinding: FragmentHomeBinding by bindView()
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.homeList.linear().setup {

        }
    }
}