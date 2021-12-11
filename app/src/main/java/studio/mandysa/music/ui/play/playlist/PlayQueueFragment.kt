package studio.mandysa.music.ui.play.playlist

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.fragment.DialogFragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlayQueueBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NewSongModel
import studio.mandysa.music.logic.utils.createAlbum
import studio.mandysa.music.logic.utils.getInstance

class PlayQueueFragment : DialogFragment() {

    private val mImageLoader = ImageLoader.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity(), R.style.TransparentDialog)
            .create().also { it ->
                val mBinding = FragmentPlayQueueBinding.inflate(
                    layoutInflater,
                    it.window!!.decorView as ViewGroup,
                    true
                )
                mBinding.recycler.linear().setup {
                    addType<NewSongModel>(R.layout.item_song)
                    addType<MusicModel>(R.layout.item_song)
                    onBind {
                        val model = getModel<DefaultMusic<DefaultArtist>>()
                        ItemSongBinding.bind(itemView).apply {
                            songName.text = model.title
                            songSingerName.text = model.artist[0].name
                            mImageLoader.displayImage(model.coverUrl, songCover)
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
                getInstance().changePlayListLiveData().lazy(this) {
                    mBinding.recycler.recyclerAdapter.models = it
                }
                val layoutParams: WindowManager.LayoutParams = it.window!!.attributes
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                it.window!!.attributes = layoutParams
                it.window!!.setGravity(Gravity.BOTTOM)
                it.window!!.setWindowAnimations(R.style.DialogAnim)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mImageLoader.clearMemoryCache()
    }
}