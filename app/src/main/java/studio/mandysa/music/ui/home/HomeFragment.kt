package studio.mandysa.music.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import mandysax.fragmentpage.widget.FragmentPage
import mandysax.navigation.Navigation
import mandysax.tablayout.NavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentHomeBinding
import studio.mandysa.music.databinding.LayoutToolbarBinding
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.ui.search.SearchFragment

class HomeFragment : Fragment() {

    private val mBinding: FragmentHomeBinding by bindView()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LayoutToolbarBinding.bind(mBinding.root).apply {
            editFrame.let {
                it.isFocusableInTouchMode = false//不可编辑
                it.keyListener = null//不可粘贴，长按不会弹出粘贴框
                //it.setClickable(false);//不可点击，但是这个效果我这边没体现出来，不知道怎没用
                it.isFocusable = false//不可编辑
            }
            editFrame.setOnClickListener {
                Navigation.findViewNavController(it).navigate(SearchFragment())
            }
            mBinding.homeFragmentPage.setAdapter(object : FragmentPage.Adapter {
                override fun onCreateFragment(position: Int): Fragment? =
                    when (position) {
                        0 -> RecommendFragment()
                        1 -> MusicHallFragment()
                        2 -> RankingFragment()
                        else -> null
                    }

            })
            topNav.models = listOf(
                NavigationItem(
                    context.getString(R.string.recommend)
                ),
                NavigationItem(
                    context.getString(R.string.music_hall)
                ),
                NavigationItem(
                    context.getString(R.string.ranking)
                )
            ).setInActiveColor(context.getColor(android.R.color.black))
                .setActiveColor(context.getColor(R.color.main))
            topNav.getSelectedPosition().observeForever {
                mBinding.homeFragmentPage.position = it
            }
            topNav.setSelectedPosition(0)
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