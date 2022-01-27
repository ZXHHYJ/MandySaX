package studio.mandysa.music.ui.home

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.addModels
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.*
import studio.mandysa.music.logic.model.BannerModels
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendSongs
import studio.mandysa.music.logic.utils.*
import studio.mandysa.music.ui.all.playlist.PlaylistFragment
import studio.mandysa.music.ui.event.UserViewModel

class RecommendFragment : Fragment() {
    val mBinding: FragmentRecommendBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            recycler.linear().setup {
                addType<PlaylistModel>(R.layout.item_recommend_playlist_rv)
                addType<RecommendSongs.RecommendSong>(R.layout.item_song)
                addType<BannerModels>(R.layout.item_banner_rv)
                val linearSnapHelper = LinearSnapHelper()
                val pagerSnapHelper = PagerSnapHelper()
                onBind {
                    when (itemViewType) {
                        R.layout.item_banner_rv -> {
                            ItemBannerRvBinding.bind(itemView).recycler.apply {
                                pagerSnapHelper.attachToRecyclerView(this)
                                linear(orientation = RecyclerView.HORIZONTAL).setup {
                                    addType<BannerModels.BannerModel>(R.layout.item_banner)
                                    onBind {
                                        val model = getModel<BannerModels.BannerModel>()
                                        ItemBannerBinding.bind(itemView).let { it ->
                                            it.planIv.setImageURI(model.pic)
                                            it.planIv.setOnClickListener {
                                                when (model.targetType) {
                                                    3000 -> {
                                                        val uri: Uri = Uri.parse(model.url)
                                                        val intent = Intent(Intent.ACTION_VIEW, uri)
                                                        startActivity(intent)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                recyclerAdapter.models = getModel<BannerModels>().list
                            }
                        }
                        R.layout.item_recommend_playlist_rv -> {
                            ItemRecommendPlaylistRvBinding.bind(itemView).playlistList.apply {
                                linearSnapHelper.attachToRecyclerView(this)
                                addItemDecoration(object : RecyclerView.ItemDecoration() {
                                    override fun getItemOffsets(
                                        outRect: Rect,
                                        view: View,
                                        parent: RecyclerView,
                                        state: RecyclerView.State
                                    ) {
                                        val length =
                                            parent.resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
                                        view.layoutParams.width =
                                            parent.resources.getDimensionPixelOffset(R.dimen.album_width) + length / 2
                                        view.setPadding(length, 0, 0, 0)
                                    }
                                }, 0)
                                linear(orientation = RecyclerView.HORIZONTAL).setup {
                                    addType<PlaylistModel.Playlist>(R.layout.item_playlist)
                                    onBind {
                                        val model = getModel<PlaylistModel.Playlist>()
                                        ItemPlaylistBinding.bind(itemView).apply {
                                            playlistTitle.text = model.name
                                            playlistCover.setImageURI(model.picUrl)
                                            playlistCover.setOnClickListener {
                                                Navigation.findViewNavController(it)
                                                    .navigate(
                                                        R.style.AppFragmentAnimStyle,
                                                        PlaylistFragment(model.id!!)
                                                    )
                                            }
                                        }
                                    }
                                }
                                recyclerAdapter.models = getModel<PlaylistModel>().playlist!!
                            }
                        }
                        R.layout.item_song -> {
                            val model = getModel<RecommendSongs.RecommendSong>()
                            ItemSongBinding.bind(itemView).apply {
                                songName.text = model.title
                                songSingerName.text = model.artist.allArtist()
                                songCover.setImageURI(model.coverUrl)
                                itemView.setOnClickListener {
                                    DefaultPlayerManager.getInstance()!!.apply {
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
                                                context.getColor(R.color.blue)
                                            )
                                            songSingerName.setTextColor(context.getColor(R.color.blue))
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
            stateLayout.showLoading {
                mEvent.getCookieLiveData().lazy {
                    NeteaseCloudMusicApi::class.java.create().apply {
                        getBannerList().set(
                            viewLifecycleOwner.lifecycle,
                            object : Callback<BannerModels> {
                                override fun onFailure(code: Int) {
                                    stateLayout.showErrorState()
                                }

                                override fun onResponse(t: BannerModels?) {
                                    if (t != null) {
                                        mBinding.recycler.recyclerAdapter.addHeader(t, 0)
                                    }
                                }
                            })
                        getRecommendedPlaylist(it).set(
                            viewLifecycleOwner.lifecycle,
                            object : Callback<PlaylistModel> {
                                override fun onResponse(t: PlaylistModel?) {
                                    stateLayout.showContentState()
                                    if (t != null) {
                                        mBinding.recycler.recyclerAdapter.addHeader(t, 1)
                                    }
                                }

                                override fun onFailure(code: Int) {
                                    stateLayout.showErrorState()
                                }

                            })
                        getRecommendedSong(it).set(viewLifecycleOwner.lifecycle,
                            object : Callback<RecommendSongs> {

                                override fun onResponse(t: RecommendSongs?) {
                                    mBinding.recycler.addModels(t!!.list!!)
                                }

                                override fun onFailure(code: Int) {
                                    stateLayout.showErrorState()
                                }

                            })
                    }
                }
            }
            stateLayout.showLoadingState()
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