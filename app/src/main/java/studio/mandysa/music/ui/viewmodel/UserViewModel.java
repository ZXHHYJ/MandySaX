package studio.mandysa.music.ui.viewmodel;

import mandysax.anna2.observable.Observable;
import mandysax.lifecycle.ViewModel;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Model.UserModel;
import studio.mandysa.music.logic.Network.ServiceCreator;

public class UserViewModel extends ViewModel {

    public Observable<UserModel> getUserModel(String cookie) {
        return ServiceCreator.create(NeteaseCloudMusicApi.class).getUserInfo(cookie, System.currentTimeMillis());
    }

}
