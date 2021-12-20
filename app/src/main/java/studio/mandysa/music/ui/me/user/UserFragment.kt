package studio.mandysa.music.ui.me.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentUserBinding
import studio.mandysa.music.logic.utils.bindView

class UserFragment : Fragment() {
    private val mBinding: FragmentUserBinding by bindView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}