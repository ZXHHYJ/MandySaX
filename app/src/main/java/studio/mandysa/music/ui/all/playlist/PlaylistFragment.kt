package studio.mandysa.music.ui.all.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.anna2.callback.Callback
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.addModel
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.linear
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlaylistBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.utils.ClassUtils.create
import studio.mandysa.music.ui.base.BaseFragment

class PlaylistFragment(private val id: String) : BaseFragment() {

    private val mBinding: FragmentPlaylistBinding by bindView()

    private var mImageLoader: ImageLoader = ImageLoader.getInstance()

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
            playlistList.linear().setup {
                addType<MusicModel>(R.layout.item_song)
                onBind {
                    val model = getModel<MusicModel>()
                    ItemSongBinding.bind(itemView).apply {
                        songName.text = model.title
                        songSingerName.text = model.artist[0].name
                        mImageLoader.displayImage(model.coverUrl, songCover)
                    }
                }
            }
        }
        NeteaseCloudMusicApi::class.java.create()
            .getSongListInfo(id)
            .set(object : Callback<PlaylistInfoModel> {
                override fun onResponse(t: PlaylistInfoModel?) {

                    NeteaseCloudMusicApi::class.java.create()
                        .getMusicInfo(t!!.songList)
                        .set(object : Callback<List<MusicModel>> {
                            override fun onResponse(t: List<MusicModel>?) {
                                mBinding.playlistList.addModel(t!!.toMutableList())
                            }

                            override fun onFailure(code: Int) {

                            }
                        })
                }

                override fun onFailure(code: Int) {

                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mImageLoader.clearMemoryCache()
    }
}