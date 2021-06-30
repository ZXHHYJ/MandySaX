package studio.mandysa.music.ui.viewmodel;

import mandysax.anna2.callback.Callback;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.livedata.LiveData;
import mandysax.lifecycle.livedata.MutableLiveData;
import simon.tuke.Tuke;
import studio.mandysa.music.logic.Model.LoginModel;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Network.ServiceCreator;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<String> mCookie = new MutableLiveData<>();

    public final String KEY_COOKIE = "cookie";

    public LoginViewModel() {
        mCookie.setValue(getCookie());
    }

    public String getCookie() {
        return Tuke.get(KEY_COOKIE, mCookie.getValue());
    }

    void saveCookie(String cookie) {
        Tuke.write(KEY_COOKIE, cookie);
    }

    public LiveData<String> getCookieLiveData() {
        return mCookie;
    }

    public void login(final String mobile, final String password) {
        ServiceCreator.create(NeteaseCloudMusicApi.class).login(mobile, password).set(new Callback<LoginModel>() {

            @Override
            public void onResponse(boolean loaded, LoginModel t) {
                saveCookie(t.cookie);
                mCookie.setValue(t.cookie);
            }

            @Override
            public void onFailure(int code) {
                mCookie.setValue(null);
            }
        });
    }

}
