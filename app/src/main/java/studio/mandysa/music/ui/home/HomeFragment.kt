package studio.mandysa.music.ui.home

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
import mandysax.media.DefaultPlayerManager.Companion.getInstance
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.jiuwo.utils.staggered
import studio.mandysa.music.R
import studio.mandysa.music.databinding.*
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.NewSongModel
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.RecommendedSongs
import studio.mandysa.music.logic.utils.*
import studio.mandysa.music.ui.all.playlist.PlaylistFragment
import studio.mandysa.music.ui.event.UserViewModel

class HomeFragment : Fragment() {

    private val mBinding: FragmentHomeBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            recycler.linear().setup {
                addType<PlaylistModel>(R.layout.item_recommended_playlist)
                addType<RecommendedSongs.RecommendedSong>(R.layout.item_song)
                val snapHelper = LinearSnapHelper()
                onBind {
                    when (itemViewType) {
                        R.layout.item_recommended_playlist -> {
                            ItemRecommendedPlaylistBinding.bind(itemView).playlistList.apply {
                                snapHelper.attachToRecyclerView(this)
                                addItemDecoration(ThreePointsDecoration())
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
                            val model = getModel<RecommendedSongs.RecommendedSong>()
                            ItemSongBinding.bind(itemView).apply {
                                songName.text = model.title
                                songSingerName.text = model.artist[0].name
                                songCover.setImageURI(model.coverUrl)
                                itemView.setOnClickListener {
                                    getInstance()!!.apply {
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
                                    getInstance()!!.changeMusicLiveData()
                                        .observe(viewLifecycleOwner, lifecycleObserver)
                                }
                                onDetached {
                                    getInstance()!!.changeMusicLiveData()
                                        .removeObserver(lifecycleObserver)
                                }
                            }
                        }
                    }
                }
            }
            statelayout.showLoading {
                mEvent.getCookieLiveData().lazy {
                    NeteaseCloudMusicApi::class.java.create().apply {
                        getRecommendedPlaylist(it).set(
                            viewLifecycleOwner.lifecycle,
                            object : Callback<PlaylistModel> {
                                override fun onResponse(t: PlaylistModel?) {
                                    statelayout.showContentState()
                                    mBinding.recycler.recyclerAdapter.headers = listOf(t)
                                }

                                override fun onFailure(code: Int) {
                                    statelayout.showErrorState()
                                }

                            })
                        getRecommendedSong(it).set(viewLifecycleOwner.lifecycle,
                            object : Callback<RecommendedSongs> {

                                override fun onResponse(t: RecommendedSongs?) {
                                    mBinding.recycler.recyclerAdapter.models = t!!.list
                                }

                                override fun onFailure(code: Int) {
                                    statelayout.showErrorState()
                                }

                            })
                        /*getRecommendedNewSong().set(
                            viewLifecycleOwner.lifecycle,
                            object : Callback<List<NewSongModel>> {
                                override fun onResponse(t: List<NewSongModel>?) {
                                    statelayout.showContentState()
                                    mBinding.recycler.recyclerAdapter.footers = t
                                }

                                override fun onFailure(code: Int) {
                                    statelayout.showErrorState()
                                }

                            })*/
                    }
                }
            }
            statelayout.showLoadingState()
        }
    }

}