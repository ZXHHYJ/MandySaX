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

    private val mCookieLiveData =
        MutableLiveData<String>(MMKV.defaultMMKV().decodeString(COOKIE_KEY))

    fun getCookieLiveData(): LiveData<String> = mCookieLiveData

    init {
        mCookieLiveData.observeForever {
            MMKV.defaultMMKV().encode(COOKIE_KEY, it)
        }
    }

    fun login(mobilePhone: String, password: String): LiveData<Int> {
        return MutableLiveData<Int>().also {
            NeteaseCloudMusicApi::class.java.create()
                .login(mobilePhone, password, System.currentTimeMillis()).set(object :
                    Callback<LoginModel> {
                    override fun onFailure(code: Int) {
                        it.value = code
                    }

                    override fun onResponse(t: LoginModel?) {
                        it.value = t!!.code
                        if (t.cookie.isNotEmpty())
                            mCookieLiveData.value = t.cookie
                    }
                })
        }
    }

}
