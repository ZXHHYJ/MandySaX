package studio.mandysa.music.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import mandysax.navigation.Navigation
import mandysax.tablayout.NavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import mandysax.viewpager.adapter.FragmentStateAdapter
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
                it.isFocusableInTouchMode = false
                it.keyListener = null
                it.isFocusable = false
            }
            editFrame.setOnClickListener {
                Navigation.findViewNavController(it).navigate(SearchFragment())
            }
            mBinding.homeViewPager.adapter = object : FragmentStateAdapter() {
                private val list = listOf(
                    RecommendFragment(), MusicHallFragment(), RankingFragment()
                )

                override fun createFragment(position: Int): Fragment {
                    return list[position]
                }

                override fun getItemCount(): Int {
                    return list.size
                }

            }
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
                .setActiveColor(context.getColor(R.color.theme_color))
            topNav.getSelectedPosition().observeForever {
                mBinding.homeViewPager.currentItem = it
            }
            mBinding.homeViewPager.setUserInputEnabled(false)
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