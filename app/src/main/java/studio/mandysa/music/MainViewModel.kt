package studio.mandysa.music

import mandysax.fragment.Fragment
import mandysax.lifecycle.ViewModel
import mandysax.navigation.fragment.NavHostFragment
import mandysax.viewpager.adapter.FragmentStateAdapter
import studio.mandysa.music.ui.home.HomeFragment
import studio.mandysa.music.ui.me.MeFragment

class MainViewModel : ViewModel() {
    val adapter = object : FragmentStateAdapter() {
        private val list = listOf(
            NavHostFragment.create(HomeFragment()), NavHostFragment.create(MeFragment())
        )

        override fun createFragment(position: Int): Fragment {
            return list[position]
        }

        override fun getItemCount(): Int {
            return list.size
        }

    }
}