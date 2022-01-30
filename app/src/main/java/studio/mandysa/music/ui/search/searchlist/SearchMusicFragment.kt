package studio.mandysa.music.ui.search.searchlist

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.jiuwo.utils.addModels
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentSearchListBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.SearchMusicModel
import studio.mandysa.music.logic.network.ServiceCreator
import studio.mandysa.music.logic.utils.*
import studio.mandysa.music.ui.search.SearchViewModel

class SearchMusicFragment(private val mViewModel: SearchViewModel) : Fragment() {

    private val mBinding: FragmentSearchListBinding by bindView()

    private var mPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.searchContentLiveData.observe(viewLifecycleOwner) { it ->
            if (!TextUtils.isEmpty(it)) {
                mBinding.let {
                    if (it.recycler.recyclerAdapter.mModel != null) {
                        it.recycler.scrollToPosition(0)
                        it.recycler.recyclerAdapter.clearModels()
                        mPage = 1
                    }
                    it.stateLayout.showLoadingState()
                }
            }
        }
        mBinding.apply {
            stateLayout.apply {
                showLoading {
                    nextPage()
                }
            }
            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = (recyclerView.layoutManager as LinearLayoutManager?)!!
                    val lastCompletelyVisibleItemPosition =
                        layoutManager.findLastCompletelyVisibleItemPosition()
                    if (lastCompletelyVisibleItemPosition == layoutManager.itemCount - 1) {
                        nextPage()
                    }
                }
            })
            recycler.linear().setup {
                addType<MusicModel>(R.layout.item_song)
                onBind {
                    ItemSongBinding.bind(itemView).run {
                        val model = getModel<MusicModel>()
                        songCover.setImageURI(model.coverUrl)
                        songName.text = model.title
                        songSingerName.text = model.artist[0].name
                        songName.markByColor(mViewModel.searchContentLiveData.value)
                        songSingerName.markByColor(mViewModel.searchContentLiveData.value)
                        itemView.setOnClickListener {
                            DefaultPlayerManager.getInstance()!!.loadAlbum(
                                models.createAlbum(),
                                modelPosition
                            )
                            DefaultPlayerManager.getInstance()!!.play()
                        }
                        val lifecycleObserver =
                            Observer<DefaultMusic<DefaultArtist>> { p1 ->
                                if (p1.id == model.id) {
                                    songName.setTextColor(
                                        context.getColor(R.color.blue)
                                    )
                                    songSingerName.setTextColor(context.getColor(R.color.blue))
                                } else {
                                    songName.setTextColor(
                                        context.getColor(android.R.color.black)
                                    )
                                    songSingerName.setTextColor(context.getColor(R.color.tv_color_light))
                                    songName.markByColor(mViewModel.searchContentLiveData.value)
                                    songSingerName.markByColor(mViewModel.searchContentLiveData.value)
                                }
                            }
                        onAttached {
                            DefaultPlayerManager.getInstance()!!.changeMusicLiveData()
                                .observe(viewLifecycleOwner, lifecycleObserver)
                        }
                        onDetached {
                            DefaultPlayerManager.getInstance()!!.changeMusicLiveData()
                                .removeObserver(lifecycleObserver)
                        }
                    }
                }
            }
        }
    }

    private fun nextPage() {
        ServiceCreator.create(NeteaseCloudMusicApi::class.java)
            .searchMusic(mViewModel.searchContentLiveData.value, (mPage - 1) * 30)
            .set(viewLifecycleOwner.lifecycle, object : Callback<List<SearchMusicModel>> {
                override fun onResponse(t: List<SearchMusicModel>?) {
                    NeteaseCloudMusicApi::class.java.create()
                        .getMusicInfo(t!!)
                        .set(viewLifecycleOwner.lifecycle, object : Callback<List<MusicModel>> {
                            override fun onResponse(t: List<MusicModel>?) {
                                mBinding.recycler.addModels(t!!)
                                mBinding.stateLayout.showContentState()
                                mPage++
                            }

                            override fun onFailure(code: Int) {
                                mBinding.stateLayout.showErrorState()
                            }
                        })
                }

                override fun onFailure(code: Int) {
                    mBinding.stateLayout.showErrorState()
                }
            })
    }

}