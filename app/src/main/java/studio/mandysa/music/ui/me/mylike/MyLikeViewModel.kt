package studio.mandysa.music.ui.me.mylike

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

class MyLikeViewModel : ViewModel() {

    private val mDataLiveData = MutableLiveData<List<MusicModel>?>()

    private val mMetadataLiveData = MutableLiveData<List<PlaylistInfoModel.SongList>?>()

    private val mPlaylistInfoModelLiveData = MutableLiveData<PlaylistInfoModel>()

    private var mObservable: Observable<List<MusicModel>>? = null

    fun initData(cookie: String, id: String): LiveData<List<MusicModel>?> {
        loadMyLikeList()
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

    fun add(id: String) {

    }

    fun remove(id: String) {

    }

    fun added(id: String): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        return liveData
    }

    fun getPlaylistInfoModelLiveData(): LiveData<PlaylistInfoModel> = mPlaylistInfoModelLiveData

    private fun loadMyLikeList() {
        if (mObservable == null)
            mMetadataLiveData.observeForever(object : Observer<List<PlaylistInfoModel.SongList>?> {
                val mThis = this
                override fun onChanged(p1: List<PlaylistInfoModel.SongList>?) {
                    if (p1 == null) {
                        mDataLiveData.value = null
                        mMetadataLiveData.removeObserver(mThis)
                        return
                    }
                    mObservable = NeteaseCloudMusicApi::class.java.create()
                        .getMusicInfo(p1).also {
                            it.set(object : Callback<List<MusicModel>> {
                                override fun onResponse(t: List<MusicModel>?) {
                                    mObservable = null
                                    mMetadataLiveData.removeObserver(mThis)
                                    if (t!!.isEmpty()) {
                                        return
                                    }
                                    mDataLiveData.value = t
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