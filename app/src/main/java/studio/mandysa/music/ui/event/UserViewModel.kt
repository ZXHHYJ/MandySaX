package studio.mandysa.music.ui.event

import com.tencent.mmkv.MMKV
import mandysax.anna2.callback.Callback
import mandysax.lifecycle.ViewModel
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import studio.mandysa.music.logic.model.LoginModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.utils.create


class UserViewModel : ViewModel() {

    companion object {
        @JvmStatic
        val COOKIE_KEY = "cookie_key"
    }

    private val mmkv = MMKV.defaultMMKV()

    private val mCookieLiveData = MutableLiveData<String>(mmkv.decodeString(COOKIE_KEY))

    fun getCookieLiveData(): LiveData<String> = mCookieLiveData

    init {
        mCookieLiveData.observeForever {
            if (it != null) {
                mmkv.encode(COOKIE_KEY, it)
            }
        }
    }

    fun login(mobilePhone: String, password: String) {
        NeteaseCloudMusicApi::class.java.create().login(mobilePhone, password).set(object :
            Callback<LoginModel> {
            override fun onFailure(code: Int) {
                mCookieLiveData.value = null
            }

            override fun onResponse(t: LoginModel?) {
                mCookieLiveData.value = t!!.cookie
            }

        })
    }

}
