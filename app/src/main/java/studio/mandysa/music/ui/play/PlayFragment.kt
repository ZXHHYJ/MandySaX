package studio.mandysa.music.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlayBinding
import studio.mandysa.music.logic.utils.FragmentUtils.bindView

class PlayFragment : Fragment() {

    private val mBinding: FragmentPlayBinding by bindView()

    private val mImageLoader = ImageLoader.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instance = DefaultPlayerManager.getInstance()!!

        /**
         * 音乐进度监听
         */
        val musicProObs = Observer<Int> {
            mBinding.playProgress.progress = it
        }
        instance.pauseLiveData().observe(viewLifecycleOwner) {
            mBinding.playOrPause.setImageResource(if (it) R.drawable.ic_play else R.drawable.ic_pause)
        }
        instance.changeMusicLiveData().observe(viewLifecycleOwner) {
            mBinding.playSongName.text = it.title
            mBinding.playSingerName.text = it.artist[0].name
            mImageLoader.displayImage(it.coverUrl, mBinding.playCover)
        }
        instance.playingMusicDurationLiveData()
            .observe(viewLifecycleOwner) {
                mBinding.playProgress.max = it
            }
        instance.playingMusicProgressLiveData()
            .observe(viewLifecycleOwner, musicProObs)
        mBinding.run {
            playProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    instance.playingMusicProgressLiveData()
                        .removeObserver(musicProObs)
                }

                override fun onStopTrackingTouch(p0: SeekBar) {
                    instance.let {
                        it.seekTo(p0.progress)
                        it.play()
                        it.playingMusicProgressLiveData()
                            .observe(viewLifecycleOwner, musicProObs)
                    }
                }

            })
            playSkipPrevious.setOnClickListener {
                instance.skipToPrevious()
            }
            playOrPause.setOnClickListener {
                instance.apply {
                    if (pauseLiveData().value == true)
                        play()
                    else pause()
                }
            }
            playSkipNext.setOnClickListener {
                instance.skipToNext()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mImageLoader.clearMemoryCache()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

}