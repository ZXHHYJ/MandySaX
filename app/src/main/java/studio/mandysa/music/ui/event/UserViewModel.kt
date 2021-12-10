package studio.mandysa.music.ui.event

import mandysax.anna2.callback.Callback
import mandysax.lifecycle.ViewModel
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import simon.tuke.Tuke
import studio.mandysa.music.logic.model.LoginModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.utils.create


class UserViewModel : ViewModel() {

    companion object {
        @JvmStatic
        val COOKIE_KEY = "cookie_key"
    }

    private val mCookieLiveData = MutableLiveData<String>(Tuke.tukeGet(COOKIE_KEY))

    fun getCookieLiveData(): LiveData<String> = mCookieLiveData

    init {
        mCookieLiveData.observeForever {
            Tuke.tukeWrite(COOKIE_KEY, it)
        }
    }

    fun login(mobilePhone: String, password: String): LiveData<Int> {
        val mCodeLiveData = MutableLiveData<Int>()
        NeteaseCloudMusicApi::class.java.create().login(mobilePhone, password).set(object :
            Callback<LoginModel> {
            override fun onFailure(code: Int) {
                mCodeLiveData.value = code
            }

            override fun onResponse(t: LoginModel?) {
                mCodeLiveData.value = t!!.code
                if (t.cookie.isNotEmpty())
                    mCookieLiveData.value = t.cookie
            }

        })
        return mCodeLiveData
    }

}
