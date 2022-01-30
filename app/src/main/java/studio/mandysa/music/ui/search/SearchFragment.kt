package studio.mandysa.music.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import mandysax.fragment.Fragment
import mandysax.fragmentpage.widget.FragmentPage
import mandysax.tablayout.NavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentSearchBinding
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.hideInput
import studio.mandysa.music.logic.utils.showInput
import studio.mandysa.music.logic.utils.viewModels
import studio.mandysa.music.ui.search.searchlist.SearchMusicFragment
import studio.mandysa.music.ui.search.searchlist.SearchPlaylistFragment
import studio.mandysa.music.ui.search.searchlist.SearchSingerFragment

class SearchFragment : Fragment() {

    private val mBinding: FragmentSearchBinding by bindView()

    private val mViewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.backIv.setOnClickListener {
            activity?.onBackPressed()
            hideInput()
        }
        mBinding.editFrame.showInput()
        mBinding.editFrame.setOnEditorActionListener { v, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                hideInput()
                mViewModel.searchContentLiveData.value = mBinding.editFrame.text.toString()
            }
            false
        }
        mBinding.searchFragmentPage.setAdapter(object : FragmentPage.Adapter {
            override fun onCreateFragment(position: Int): Fragment? = when (position) {
                0 -> SearchMusicFragment(mViewModel)
                1 -> SearchSingerFragment(mViewModel)
                2 -> SearchPlaylistFragment(mViewModel)
                else -> null
            }

        })
        mBinding.topNav.models = listOf(
            NavigationItem(
                "歌曲"
            ),
            NavigationItem(
                "歌手"
            ),
            NavigationItem(
                "歌单"
            )
        ).setInActiveColor(context.getColor(android.R.color.black))
            .setActiveColor(context.getColor(R.color.main))
        mBinding.topNav.getSelectedPosition().observeForever {
            mBinding.searchFragmentPage.position = it
        }
        mBinding.topNav.setSelectedPosition(0)
    }

    private fun hideInput() {
        mBinding.editFrame.let {
            if (it.isFocused) {
                it.clearFocus()
                it.hideInput()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

}
