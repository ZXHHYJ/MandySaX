package studio.mandysa.music.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.anna2.callback.Callback
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.addModel
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.linear
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentHomeBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.NewSongModel
import studio.mandysa.music.logic.model.PlaylistModel
import studio.mandysa.music.logic.utils.ClassUtils.create
import studio.mandysa.music.ui.base.BaseFragment

class HomeFragment : BaseFragment() {

    private val mBinding: FragmentHomeBinding by bindView()

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.homeList.linear().setup {
            addType<PlaylistModel>(R.layout.layout_rv)
            addType<NewSongModel>(R.layout.item_song)
            onBind {
                when (getModelOrNull<Any>()) {
                    is PlaylistModel -> {
                    }
                    is NewSongModel -> {
                        val model = getModel<NewSongModel>()
                        ItemSongBinding.bind(getItemView()).apply {
                            songName.text = model.title
                            songSingerName.text = model.artist[0].name
                            songCover.setImageURI(model.coverUrl)
                        }
                    }
                }
            }
        }
        NeteaseCloudMusicApi::class.java.create().apply {
            recommendedPlaylist.set(object : Callback<PlaylistModel> {
                override fun onResponse(t: PlaylistModel?) {
                }

                override fun onFailure(code: Int) {
                }

            })
            recommendedSong.set(object : Callback<List<NewSongModel>> {
                override fun onResponse(t: List<NewSongModel>?) {
                    mBinding.homeList.addModel(t!!)
                }

                override fun onFailure(code: Int) {

                }

            })
        }
    }
}