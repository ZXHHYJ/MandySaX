package studio.mandysa.music.ui.searchhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import studio.mandysa.music.databinding.FragmentSearchHistoryBinding
import studio.mandysa.music.ui.base.BaseFragment

class SearchHistoryFragment : BaseFragment() {

    private val mBinding: FragmentSearchHistoryBinding by bindView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}