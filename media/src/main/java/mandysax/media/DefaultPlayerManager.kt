package mandysax.media

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import mandysax.media.model.DefaultAlbum
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import java.io.IOException
import java.util.*

/**
 * @author liuxiaoliu66
 */
class DefaultPlayerManager(
    private val mContext: Context?
) : PlayerManagerImpl<DefaultAlbum> {
    /**
     * 播放器的歌曲加载状态
     */
    private var mLoaded = false

    /**
     * 播放器
     */
    private val mMediaPlayer = MediaPlayer()

    /**
     * 当前播放的歌曲
     */
    private val mChangeMusic = MutableLiveData<DefaultMusic<DefaultArtist>>()

    /**
     * 播放列表
     */
    private val mPlayList = MutableLiveData<List<DefaultMusic<DefaultArtist>>?>()

    /**
     * 播放状态
     */
    private val mPause = MutableLiveData(true)

    /**
     * 当前播放歌曲的下标
     */
    private val mIndex = MutableLiveData(0)

    /**
     * 当前播放歌曲进度
     */
    private val mProgress = MutableLiveData<Int>()

    /**
     * 当前播放歌曲时长
     */
    private val mDuration = MutableLiveData<Int>()

    private var mTimer: Timer? = null

    override fun changePlayListLiveData(): LiveData<List<DefaultMusic<DefaultArtist>>?> {
        return mPlayList
    }

    override fun playingMusicProgressLiveData(): LiveData<Int> {
        return mProgress
    }

    override fun playingMusicDurationLiveData(): LiveData<Int> {
        return mDuration
    }

    override fun changeMusicLiveData(): LiveData<DefaultMusic<DefaultArtist>> {
        return mChangeMusic
    }

    override fun pauseLiveData(): LiveData<Boolean> {
        return mPause
    }

    override fun loadAlbum(albumModel: DefaultAlbum) {
        loadAlbum(albumModel, 0)
    }

    override fun loadAlbum(albumModel: DefaultAlbum, index: Int) {
        val list = ArrayList<DefaultMusic<DefaultArtist>>()
        var i = 0
        while (i < albumModel.size()) {
            list.add(albumModel[i])
            i++
        }
        mPlayList.value = list
        updateIndex(index)
    }

    override fun seekTo(position: Int) {
        mProgress.value = position
        mMediaPlayer.seekTo(position)
    }

    private fun updateIndex(index: Int) {
        if (!(index >= 0 && mPlayList.value != null && index <= mPlayList.value!!.size - 1)) {
            pause()
            return
        }
        mIndex.value = index
    }

    override fun skipToPrevious() {
        updateIndex(mIndex.value!! - 1)
    }

    override fun skipToNext() {
        updateIndex(mIndex.value!! + 1)
    }

    override fun play() {
        if (!mLoaded) {
            return
        }
        if (mTimer == null) {
            mTimer = Timer().also {
                it.schedule(object : TimerTask() {
                    override fun run() {
                        mProgress.postValue(mMediaPlayer.currentPosition)
                    }
                }, 0, 1000)
            }
        }
        if (mPause.value != false)
            mPause.value = false
        if (!mMediaPlayer.isPlaying)
            mMediaPlayer.start()
    }

    override fun pause() {
        mTimer?.cancel()
        mTimer = null
        if (mPause.value != true)
            mPause.value = true
        mMediaPlayer.pause()
    }

    private fun playMusic(musicModel: DefaultMusic<DefaultArtist>) {
        mLoaded = false
        mChangeMusic.value = musicModel
        mMediaPlayer.reset()
        mMediaPlayer.setOnPreparedListener {
            mLoaded = true
            play()
            mDuration.value = mMediaPlayer.duration
        }
        mMediaPlayer.setOnErrorListener { _: MediaPlayer?, _: Int, _: Int ->
            pause()
            false
        }
        mMediaPlayer.setOnCompletionListener {
            skipToNext()
        }
        try {
            mMediaPlayer.setDataSource(mContext!!, Uri.parse(musicModel.url))
            mMediaPlayer.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        private var instance: DefaultPlayerManager? = null

        @JvmStatic
        fun getInstance(): DefaultPlayerManager? {
            return instance
        }

        @JvmStatic
        fun init(context: Context) {
            instance = DefaultPlayerManager(context)
        }

    }

    init {
        mIndex.observeForever { p1: Int ->
            if (mPlayList.value != null) {
                val musicModel: DefaultMusic<DefaultArtist> = mPlayList.value!![p1]
                playMusic(musicModel)
            }
        }
    }

    override fun stop() {
        //停止并释放MediaPlayer
        mMediaPlayer.stop()
        mMediaPlayer.release()
        //清除Timer
        mTimer?.cancel()
        mTimer = null
        //删除实例
        instance = null
    }

}