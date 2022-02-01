package studio.mandysa.music.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentRankingBinding
import studio.mandysa.music.databinding.ItemToplistBinding
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.ToplistModel
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.set
import studio.mandysa.music.ui.all.playlist.PlaylistFragment

class RankingFragment : Fragment() {
    private val mBinding: FragmentRankingBinding by bindView()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.let { it ->
            it.stateLayout.showLoading {
                NeteaseCloudMusicApi::class.java.create().getToplist()
                    .set(viewLifecycleOwner.lifecycle, object : Callback<List<ToplistModel>> {
                        override fun onFailure(code: Int) {
                            it.stateLayout.showErrorState()
                        }

                        override fun onResponse(t: List<ToplistModel>?) {
                            it.recycler.recyclerAdapter.models = t
                            it.stateLayout.showContentState()
                        }
                    })
            }
            it.stateLayout.showLoadingState()
            it.recycler.linear().setup {
                addType<ToplistModel>(R.layout.item_toplist)
                onBind {
                    val model = getModel<ToplistModel>()
                    ItemToplistBinding.bind(itemView).let {
                        it.cover.setImageURI(model.coverImgUrl)
                        it.title.text = model.name
                        itemView.setOnClickListener {
                            Navigation.findViewNavController(it)
                                .navigate(
                                    R.style.AppFragmentAnimStyle,
                                    PlaylistFragment(model.id!!)
                                )
                        }
                    }
                }
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