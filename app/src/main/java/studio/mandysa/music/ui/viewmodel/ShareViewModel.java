package studio.mandysa.music.ui.viewmodel;

import mandysax.anna2.callback.Callback;
import mandysax.fragment.FragmentActivity;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.livedata.LiveData;
import mandysax.lifecycle.livedata.MutableLiveData;
import studio.mandysa.music.service.Media;
import studio.mandysa.music.service.PlayManager;
import studio.mandysa.music.service.PlayManagerImpl;
import studio.mandysa.music.logic.Model.MusicModel;
import studio.mandysa.music.logic.Model.NeteaseCloudMusicApi;
import studio.mandysa.music.logic.Network.ServiceCreator;

public class ShareViewModel extends ViewModel {

    private final MutableLiveData<String> mPicUrl = new MutableLiveData<>();

    public final LiveData<String> picUrl = mPicUrl;

    public ShareViewModel(FragmentActivity activity) {
        PlayManagerImpl pmi = PlayManager.getInstance(activity);
        pmi.getMediaMetadataLiveData().observeForever(p1 -> {
            if (p1 == null) return;
            ServiceCreator.create(NeteaseCloudMusicApi.class).getMusicInfo(p1.getString(Media.MEDIA_ID)).set(new Callback<MusicModel>() {

                @Override
                public void onResponse(boolean loaded, MusicModel t) {
                    mPicUrl.postValue(t.picUrl);
                }

                @Override
                public void onFailure(int code) {
                }
            });
        });
    }
}
