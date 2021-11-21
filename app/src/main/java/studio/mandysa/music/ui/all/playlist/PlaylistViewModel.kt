package studio.mandysa.music.ui.all.playlist

import mandysax.anna2.callback.Callback
import mandysax.anna2.observable.Observable
import mandysax.lifecycle.ViewModel
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import mandysax.lifecycle.livedata.Observer
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.logic.utils.create
import kotlin.math.abs

class PlaylistViewModel : ViewModel() {

    private val mDataLiveData = MutableLiveData<List<MusicModel>?>()

    private val mMetadataLiveData = MutableLiveData<List<PlaylistInfoModel.SongList>?>()

    private val mPlaylistInfoModelLiveData = MutableLiveData<PlaylistInfoModel>()

    private var mObservable: Observable<List<MusicModel>>? = null

    private var mPage = 0

    fun initData(id: String): LiveData<List<MusicModel>?> {
        nextPage()
        NeteaseCloudMusicApi::class.java.create()
            .getSongListInfo(id)
            .set(object : Callback<PlaylistInfoModel> {
                override fun onResponse(t: PlaylistInfoModel?) {
                    mPlaylistInfoModelLiveData.value = t!!
                    mMetadataLiveData.value = t.songList
                }

                override fun onFailure(code: Int) {
                    mMetadataLiveData.value = null
                }
            })
        return mDataLiveData
    }

    fun getPlaylistInfoModelLiveData(): LiveData<PlaylistInfoModel> = mPlaylistInfoModelLiveData

    fun nextPage() {
        if (mObservable == null)
        mMetadataLiveData.observeForever(object : Observer<List<PlaylistInfoModel.SongList>?> {
            val mThis = this
            override fun onChanged(p1: List<PlaylistInfoModel.SongList>?) {
                if (p1 == null) {
                    mDataLiveData.value = null
                    mMetadataLiveData.removeObserver(mThis)
                    return
                }
                val list = ArrayList<PlaylistInfoModel.SongList>()
                val difference = 30 * mPage
                if (difference > mMetadataLiveData.value!!.size)
                    return
                for (i in difference until if (mMetadataLiveData.value!!.size - difference < 30)
                    difference + abs(mMetadataLiveData.value!!.size - difference)
                else
                    30 * (mPage + 1)) {
                    list.add(mMetadataLiveData.value!![i])
                }
                mObservable = NeteaseCloudMusicApi::class.java.create()
                    .getMusicInfo(list).also {
                        it.set(object : Callback<List<MusicModel>> {
                            override fun onResponse(t: List<MusicModel>?) {
                                mDataLiveData.value = t!!
                                mPage++
                                mObservable = null
                                mMetadataLiveData.removeObserver(mThis)
                            }

                            override fun onFailure(code: Int) {
                                mDataLiveData.value = null
                                mObservable = null
                                mMetadataLiveData.removeObserver(mThis)
                            }
                        })
                    }
            }

        })
    }
}