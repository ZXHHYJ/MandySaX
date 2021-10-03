package studio.mandysa.music.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.mandysa.music.databinding.FragmentMeBinding
import studio.mandysa.music.ui.base.BaseFragment

class MeFragment : BaseFragment() {

    private val mBinding: FragmentMeBinding by bindView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle
    ): View {
        return mBinding.root
    }
}