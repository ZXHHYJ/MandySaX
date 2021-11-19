package studio.mandysa.music.ui.all.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.addHeader
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.addModels
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.linear
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlaylistBinding
import studio.mandysa.music.databinding.ItemPlaylistInfoHeadBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.utils.*

class PlaylistFragment(private val id: String) : Fragment() {

    private val mBinding: FragmentPlaylistBinding by bindView()

    private val mViewModel: PlaylistViewModel by viewModels()

    private val mImageLoader = ImageLoader.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.let { it ->
            it.statelayout.apply {
                showLoading {
                    mViewModel.initData(this@PlaylistFragment.id).observe(viewLifecycleOwner) {
                        if (it == null) {
                            showErrorState()
                            return@observe
                        }
                        mBinding.recycler.addModels(it)
                        showContentState()
                    }
                    mViewModel.getPlaylistInfoModelLiveData().observe(viewLifecycleOwner){
                        mBinding.recycler.addHeader(it)
                    }
                }
                showLoadingState()
            }
        }
        mBinding.recycler.linear().setup {
            addType<PlaylistInfoModel>(R.layout.item_playlist_info_head)
            addType<MusicModel>(R.layout.item_song)
            onBind {
                when (itemViewType) {
                    R.layout.item_playlist_info_head -> {
                        val model = getModel<PlaylistInfoModel>()
                        ItemPlaylistInfoHeadBinding.bind(itemView).apply {
                            mImageLoader.displayImage(model.coverImgUrl, cover)
                            title.text = model.name
                            todo.text = model.description
                        }
                    }
                    R.layout.item_song -> {
                        val model = getModel<MusicModel>()
                        ItemSongBinding.bind(itemView).apply {
                            songName.text = model.title
                            songSingerName.text = model.artist[0].name
                            mImageLoader.displayImage(model.coverUrl, songCover)
                            itemView.setOnClickListener {
                                getInstance().apply {
                                    loadAlbum(
                                        getModels<DefaultMusic<DefaultArtist>>().createAlbum(),
                                        modelPosition
                                    )
                                    play()
                                }
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
        mBinding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager?)!!
                val lastCompletelyVisibleItemPosition =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                if (lastCompletelyVisibleItemPosition == layoutManager.itemCount - 1) {
                    mViewModel.nextPage()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mImageLoader.clearMemoryCache()
    }

}