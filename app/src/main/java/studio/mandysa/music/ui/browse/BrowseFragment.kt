package studio.mandysa.music.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import mandysax.fragment.FragmentTransaction
import mandysax.navigation.Navigation
import mandysax.tablayout.NavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import mandysax.viewpager.adapter.FragmentStateAdapter
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentBrowseBinding
import studio.mandysa.music.databinding.LayoutToolbarBinding
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.ui.search.SearchFragment

class BrowseFragment : Fragment() {
    private val mBinding by bindView<FragmentBrowseBinding>()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewPager.let {
            it.setUserInputEnabled(false)
            it.adapter = object : FragmentStateAdapter() {

                private val list = listOf(MeFragment(), DatabaseFragment())

                override fun createFragment(position: Int): Fragment {
                    return list[position]
                }

                override fun getItemCount(): Int {
                    return list.size
                }

                override fun beginTransaction(): FragmentTransaction =
                    childFragmentManager.beginTransaction()

            }
        }
        LayoutToolbarBinding.bind(mBinding.root).apply {
            editFrame.let {
                it.isFocusableInTouchMode = false
                it.keyListener = null
                it.isFocusable = false
            }
            editFrame.setOnClickListener {
                Navigation.findViewNavController(it).navigate(SearchFragment())
            }
            topNav.models = listOf(
                NavigationItem(
                    context.getString(R.string.me)
                ),
                NavigationItem("资料库")
            ).setInActiveColor(context.getColor(android.R.color.black))
                .setActiveColor(context.getColor(R.color.main))
            topNav.getSelectedPosition().observeForever {
                mBinding.viewPager.setCurrentItem(it)
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