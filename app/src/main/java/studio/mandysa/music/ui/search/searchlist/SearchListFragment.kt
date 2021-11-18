package studio.mandysa.music.ui.search.searchlist

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.anna2.callback.Callback
import mandysax.lifecycle.ViewModelProviders
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.linear
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.recyclerAdapter
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentSearchListBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.SearchMusicModel
import studio.mandysa.music.logic.network.ServiceCreator
import studio.mandysa.music.logic.utils.ArrayListUtils.createAlbum
import studio.mandysa.music.logic.utils.BindingAdapterUtils.getModels
import studio.mandysa.music.logic.utils.ClassUtils.create
import studio.mandysa.music.logic.utils.TextViewUtils.markByColor
import studio.mandysa.music.ui.base.BaseFragment
import studio.mandysa.music.ui.search.SearchViewModel

class SearchListFragment : BaseFragment() {

    private val mBinding: FragmentSearchListBinding by bindView()

    private val mImageLoader = ImageLoader.getInstance()

    private var mViewModel: SearchViewModel? = null

    private var mPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = ViewModelProviders.of(activity)[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel!!.SearchContentLiveData.observe(viewLifecycleOwner) {
            if (!TextUtils.isEmpty(it)) {
                mBinding.statelayout.showLoadingState()
            }
        }
        mBinding.apply {
            statelayout.apply {
                showLoading {
                    nextPage()
                }
            }
            recycler.linear().setup {
                addType<MusicModel>(R.layout.item_song)
                onBind {
                    ItemSongBinding.bind(itemView).run {
                        val model = getModel<MusicModel>()
                        mImageLoader.displayImage(model.coverUrl, songCover)
                        songName.text = model.title
                        songSingerName.text = model.artist[0].name
                        songName.markByColor(mViewModel!!.SearchContentLiveData.value)
                        songSingerName.markByColor(mViewModel!!.SearchContentLiveData.value)
                        itemView.setOnClickListener {
                            DefaultPlayerManager.getInstance()!!.loadAlbum(
                                getModels<DefaultMusic<DefaultArtist>>().createAlbum(),
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
                                    songSingerName.setTextColor(context.getColor(R.color.test2))
                                    songName.markByColor(mViewModel!!.SearchContentLiveData.value)
                                    songSingerName.markByColor(mViewModel!!.SearchContentLiveData.value)
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
            .searchMusic(mViewModel!!.SearchContentLiveData.value, (mPage - 1) * 30)
            .set(object : Callback<List<SearchMusicModel>> {
                override fun onResponse(t: List<SearchMusicModel>?) {
                    NeteaseCloudMusicApi::class.java.create()
                        .getMusicInfo(t!!)
                        .set(object : Callback<List<MusicModel>> {
                            override fun onResponse(t: List<MusicModel>?) {
                                mBinding.recycler.recyclerAdapter.models = t!!
                                mBinding.statelayout.showContentState()
                                mPage++
                            }

                            override fun onFailure(code: Int) {
                                mBinding.statelayout.showErrorState()
                            }
                        })
                    //mBinding.recycler.recyclerAdapter.models = t!!
                    /* when (mBinding.searchStateLayout.status) {
                         Status.CONTENT -> {
                         }
                         else -> mBinding.searchStateLayout.showContent()
                     }*/
                }

                override fun onFailure(code: Int) {
                    mBinding.statelayout.showErrorState()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel = null
        mImageLoader.clearMemoryCache()
    }
}