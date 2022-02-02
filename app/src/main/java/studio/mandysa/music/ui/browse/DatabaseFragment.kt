package studio.mandysa.music.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentDatabaseBinding
import studio.mandysa.music.logic.utils.bindView

class DatabaseFragment : Fragment() {
    private val mBinding by bindView<FragmentDatabaseBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}