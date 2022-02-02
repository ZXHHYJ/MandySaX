package studio.mandysa.music.ui.browse.mylike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentMyLikeBinding
import studio.mandysa.music.logic.utils.bindView

class MyLikeFragment(private val playlistId: String) : Fragment() {
    private val mBinding: FragmentMyLikeBinding by bindView()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}