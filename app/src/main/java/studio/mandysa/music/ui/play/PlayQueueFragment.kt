package studio.mandysa.music.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlayQueueBinding
import studio.mandysa.music.databinding.ItemSongMiniBinding
import studio.mandysa.music.databinding.LayoutPlayToolbarBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NewSongModel
import studio.mandysa.music.logic.model.RecommendSongs
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.createAlbum
import studio.mandysa.music.logic.utils.getInstance

class PlayQueueFragment : Fragment() {

    private val mBinding: FragmentPlayQueueBinding by bindView()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = LayoutPlayToolbarBinding.bind(mBinding.root)
        getInstance().changeMusicLiveData().observe(viewLifecycleOwner) { model ->
            toolbar.songName.text = model.title
            toolbar.songSingerName.text = model.artist[0].name
            toolbar.songCover.setImageURI(model.coverUrl)
        }
        mBinding.recycler.linear().setup {
            addType<NewSongModel>(R.layout.item_song_mini)
            addType<MusicModel>(R.layout.item_song_mini)
            addType<RecommendSongs.RecommendSong>(R.layout.item_song_mini)
            onBind {
                val model = getModel<DefaultMusic<DefaultArtist>>()
                ItemSongMiniBinding.bind(itemView).apply {
                    songName.text = model.title
                    songSingerName.text = model.artist[0].name
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
        getInstance().changePlayListLiveData().lazy(this) {
            mBinding.recycler.recyclerAdapter.models = it
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