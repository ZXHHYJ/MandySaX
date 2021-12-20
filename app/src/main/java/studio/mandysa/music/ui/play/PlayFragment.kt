package studio.mandysa.music.ui.play

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.widget.SeekBar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import mandysax.fragment.Fragment
import mandysax.lifecycle.livedata.Observer
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlayBinding
import studio.mandysa.music.logic.utils.BitmapUtil
import studio.mandysa.music.logic.utils.FastBlurUtil
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.getInstance
import studio.mandysa.music.ui.play.playlist.PlayQueueFragment


class PlayFragment : Fragment(), ImageLoadingListener {

    private val mBinding: FragmentPlayBinding by bindView()

    private val mImageLoader = ImageLoader.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instance = getInstance()
        mBinding.playingList.setOnClickListener {
            PlayQueueFragment().show(activity!!.fragmentPlusManager)
        }
        mBinding.like.setOnClickListener {

        }
        ViewCompat.setOnApplyWindowInsetsListener(
            view
        ) { _, insets ->
            val startBarSize = insets!!.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarSize =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            mBinding.playLayout.setPadding(0, startBarSize, 0, navigationBarSize)
            insets
        }
        mBinding.let { it ->
            /**
             * 音乐进度监听
             */
            val musicProObs = Observer<Int> { progress ->
                it.playbackSeekbar.progress = progress
            }
            /*
             * 播放暂停按钮icon更新
             */
            instance.pauseLiveData().observe(viewLifecycleOwner) { pause ->
                if (pause) {
                    it.playBackground.pause()
                } else {
                    it.playBackground.resume()
                }
                val smallScale = if (pause) 0.9F else 1F
                val oneScale = if (!pause) 0.9F else 1F
                val scaleAnim = ScaleAnimation(
                    oneScale,
                    smallScale,
                    oneScale,
                    smallScale,
                    it.musicCoverCardView.width / 2f,
                    it.musicCoverCardView.width / 2f
                )
                scaleAnim.duration = 100
                scaleAnim.fillAfter = true
                it.musicCoverCardView.startAnimation(scaleAnim)
                it.playOrPause.setImageResource(if (pause) R.drawable.ic_play else R.drawable.ic_pause)
            }
            /**
             * 更新当前播放歌曲的信息
             */
            instance.changeMusicLiveData().observe(viewLifecycleOwner) { model ->
                it.songName.text = model.title
                it.singerName.text = model.artist[0].name
                mImageLoader.displayImage(model.coverUrl, it.musicCover, this)
            }
            /**
             * 更新播放进度
             */
            instance.playingMusicDurationLiveData()
                .observe(viewLifecycleOwner) { duration ->
                    it.playbackSeekbar.max = duration
                }
            instance.playingMusicProgressLiveData()
                .observe(viewLifecycleOwner, musicProObs)
            it.playbackSeekbar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
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
            it.playSkipPrevious.setOnClickListener {
                instance.skipToPrevious()
            }
            it.playOrPause.setOnClickListener {
                instance.apply {
                    if (pauseLiveData().value == true)
                        play()
                    else pause()
                }
            }
            it.playSkipNext.setOnClickListener {
                instance.skipToNext()
            }
            mBinding.volumeSeekbar.let {
                val volumeChangeHelper = VolumeChangeHelper(context)
                volumeChangeHelper.registerVolumeChangeListener(object :
                    VolumeChangeHelper.VolumeChangeListener {
                    override fun onVolumeDownToMin() {
                        it.progress = 0
                    }

                    override fun onVolumeUp() {
                        it.progress = volumeChangeHelper.getStreamVolume()
                    }

                })
                it.max = volumeChangeHelper.getStreamMaxVolume()
                it.progress = volumeChangeHelper.getStreamVolume()
                it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if (p1 != volumeChangeHelper.getStreamVolume())
                            volumeChangeHelper.setStreamMusic(p1)
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar) {

                    }

                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (getInstance().pauseLiveData().value == false)
            mBinding.playBackground.resume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.playBackground.pause()
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

    override fun onLoadingStarted(imageUri: String?, view: View?) {
    }

    override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
    }

    override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
        var blurBitmap = loadedImage!!
        blurBitmap = scaleBitmap(blurBitmap, 150, blurBitmap.height * 150 / blurBitmap.width)
        blurBitmap = FastBlurUtil.doBlur(blurBitmap, 25, false)
        blurBitmap = scaleBitmap(blurBitmap, blurBitmap.width, blurBitmap.height)
        blurBitmap = FastBlurUtil.doBlur(blurBitmap, 24, false)
        blurBitmap = BitmapUtil.handleImageEffect(blurBitmap, 1.8f)
        val randomTransitionGenerator = RandomTransitionGenerator()
        randomTransitionGenerator.setTransitionDuration(3000)
        mBinding.playBackground.let {
            it.setImageBitmap(blurBitmap)
            it.setTransitionGenerator(randomTransitionGenerator)
        }
    }

    override fun onLoadingCancelled(imageUri: String?, view: View?) {
    }

    private fun scaleBitmap(origin: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val height = origin.height
        val width = origin.width
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight) // 使用后乘
        return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
    }

}