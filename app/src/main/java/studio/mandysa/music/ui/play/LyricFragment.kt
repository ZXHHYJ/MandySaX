package studio.mandysa.music.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentLyricBinding
import studio.mandysa.music.logic.utils.bindView

class LyricFragment : Fragment() {

    private val mBinding: FragmentLyricBinding by bindView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}