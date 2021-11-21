package studio.mandysa.music.ui.event

import android.R.attr
import mandysax.lifecycle.ViewModel
import mandysax.lifecycle.livedata.LiveData
import mandysax.lifecycle.livedata.MutableLiveData
import studio.mandysa.music.logic.model.UserModel
import android.R.attr.path

import io.fastkv.FastKV




class UserViewModel : ViewModel() {

    private val mCookieLiveData = MutableLiveData<String>()

    private val mUserListLiveData = MutableLiveData<List<UserModel>>()

    fun getCookieLiveData(): LiveData<String> = mCookieLiveData

    fun getUserListLiveData(): LiveData<List<UserModel>> = mUserListLiveData

    init {

    }

}