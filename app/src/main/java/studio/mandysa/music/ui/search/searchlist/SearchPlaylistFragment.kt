package studio.mandysa.music.ui.search.searchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentSearchListBinding
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.ui.search.SearchViewModel

class SearchPlaylistFragment(private val mViewModel: SearchViewModel) : Fragment() {
    private val mBinding: FragmentSearchListBinding by bindView()

    private var mPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}