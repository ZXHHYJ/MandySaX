package studio.mandysa.music.ui.all.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.anna2.callback.Callback
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
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.set

class PlaylistFragment(private val id: String) : Fragment() {

    private val mBinding: FragmentPlaylistBinding by bindView()

    //private val mViewModel: PlaylistViewModel by viewModels()

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
        mBinding.apply {
            statelayout.let {
                it.showLoading {
                    NeteaseCloudMusicApi::class.java.create()
                        .getSongListInfo(this@PlaylistFragment.id)
                        .set(viewLifecycleOwner.lifecycle, object : Callback<PlaylistInfoModel> {
                            override fun onResponse(t: PlaylistInfoModel?) {
                                mBinding.recycler.addHeader(t!!)
                                NeteaseCloudMusicApi::class.java.create()
                                    .getMusicInfo(t.songList)
                                    .set(viewLifecycleOwner.lifecycle,
                                        object : Callback<List<MusicModel>> {
                                            override fun onResponse(t: List<MusicModel>?) {
                                                mBinding.recycler.addModels(t!!.toMutableList())
                                                it.showContentState()
                                            }

                                            override fun onFailure(code: Int) {
                                                it.showErrorState()
                                            }
                                        })
                            }

                            override fun onFailure(code: Int) {
                                it.showErrorState()
                            }
                        })
                }
                it.showLoadingState()
            }
            recycler.linear().setup {
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mImageLoader.clearMemoryCache()
    }

}