package studio.mandysa.music.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentMusichallBinding
import studio.mandysa.music.databinding.ItemPlaylistBinding
import studio.mandysa.music.databinding.ItemPlaylistSquareRvBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.NewSongModel
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.model.PlaylistSquareModel
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.createAlbum
import studio.mandysa.music.logic.utils.set
import studio.mandysa.music.ui.all.playlist.PlaylistFragment

class MusicHallFragment : Fragment() {
    private val mBinding: FragmentMusichallBinding by bindView()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.stateLayout.apply {
            showLoading {
                NeteaseCloudMusicApi::class.java.create().apply {
                    getPlaylistSquare().set(viewLifecycleOwner.lifecycle,
                        object : Callback<PlaylistSquareModel> {
                            override fun onFailure(code: Int) {
                                showErrorState()
                            }

                            override fun onResponse(t: PlaylistSquareModel?) {
                                mBinding.recycler.recyclerAdapter.models = listOf(t)
                                showContentState()
                            }
                        })
                    getRecommendedNewSong().set(
                        viewLifecycleOwner.lifecycle,
                        object : Callback<List<NewSongModel>> {
                            override fun onResponse(t: List<NewSongModel>?) {
                                mBinding.stateLayout.showContentState()
                                mBinding.recycler.recyclerAdapter.footers = t
                            }

                            override fun onFailure(code: Int) {
                                mBinding.stateLayout.showErrorState()
                            }

                        })
                }
            }
            showLoadingState()
            mBinding.recycler.linear().setup {
                addType<PlaylistSquareModel>(R.layout.item_playlist_square_rv)
                addType<NewSongModel>(R.layout.item_song)
                val snapHelper = LinearSnapHelper()
                onBind {
                    when (itemViewType) {
                        R.layout.item_playlist_square_rv -> {
                            ItemPlaylistSquareRvBinding.bind(itemView).apply {
                                snapHelper.attachToRecyclerView(recycler)
                                recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
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
                                })
                                recycler.linear(orientation = RecyclerView.HORIZONTAL).setup {
                                    addType<PlaylistModel>(R.layout.item_playlist)
                                    onBind {
                                        val model = getModel<PlaylistModel>()
                                        ItemPlaylistBinding.bind(itemView).apply {
                                            playlistCover.setImageURI(model.picUrl)
                                            playlistTitle.text = model.name
                                            itemView.setOnClickListener {
                                                Navigation.findViewNavController(it)
                                                    .navigate(
                                                        R.style.AppFragmentAnimStyle,
                                                        PlaylistFragment(model.id)
                                                    )
                                            }
                                        }
                                    }
                                }
                                recycler.recyclerAdapter.models =
                                    getModel<PlaylistSquareModel>().playlist
                            }
                        }
                        R.layout.item_song -> {
                            val model = getModel<NewSongModel>()
                            ItemSongBinding.bind(itemView).apply {
                                songName.text = model.title
                                songSingerName.text = model.artist[0].name
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