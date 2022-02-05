package studio.mandysa.music.ui.all.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.jiuwo.utils.addHeader
import studio.mandysa.jiuwo.utils.addModels
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlaylistBinding
import studio.mandysa.music.databinding.ItemPlaylistHeadBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.utils.*
import studio.mandysa.music.ui.event.UserViewModel

class PlaylistFragment(private val id: String) : Fragment() {

    private val mBinding: FragmentPlaylistBinding by bindView()

    private val mViewModel: PlaylistViewModel by viewModels()

    private val mUserViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.backIv.setOnClickListener {
            activity?.onBackPressed()
        }
        mBinding.stateLayout.apply {
            showLoading {
                mUserViewModel.getCookieLiveData().lazy(viewLifecycleOwner) { it ->
                    mViewModel.initData(it, this@PlaylistFragment.id)
                        .observe(viewLifecycleOwner) { it1 ->
                            if (it1 == null) {
                                showErrorState()
                                return@observe
                            }
                            mBinding.recycler.addModels(it1)
                        }
                    mViewModel.getPlaylistInfoModelLiveData().observe(viewLifecycleOwner) {
                        mBinding.recycler.addHeader(it)
                        showContentState()
                    }
                }
            }
            showLoadingState()
        }
        mBinding.recycler.linear().setup {
            addType<PlaylistInfoModel>(R.layout.item_playlist_head)
            addType<MusicModel>(R.layout.item_song)
            onBind {
                when (itemViewType) {
                    R.layout.item_playlist_head -> {
                        val model = getModel<PlaylistInfoModel>()
                        ItemPlaylistHeadBinding.bind(itemView).apply {
                            cover.setImageURI(model.coverImgUrl)
                            playlistTitle.text = model.name
                            playlistInfo.text = model.description
                        }
                    }
                    R.layout.item_song -> {
                        val model = getModel<MusicModel>()
                        ItemSongBinding.bind(itemView).apply {
                            songName.text = model.title
                            songSingerName.text = model.artist[0].name
                            songCover.setImageURI(model.coverUrl)
                            itemView.setOnClickListener {
                                getInstance().apply {
                                    loadAlbum(
                                        models.createAlbum(),
                                        modelPosition
                                    )
                                    play()
                                }
                            }
                            val lifecycleObserver =
                                Observer<DefaultMusic<DefaultArtist>> { p1 ->
                                    if (p1.id == model.id) {
                                        songName.setTextColor(
                                            context.getColor(R.color.secondary_color)
                                        )
                                        songSingerName.setTextColor(context.getColor(R.color.secondary_color))
                                    } else {
                                        songName.setTextColor(
                                            context.getColor(android.R.color.black)
                                        )
                                        songSingerName.setTextColor(context.getColor(R.color.tv_color_light))
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

}