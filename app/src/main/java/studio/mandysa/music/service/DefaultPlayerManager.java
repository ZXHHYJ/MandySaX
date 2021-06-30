package studio.mandysa.music.service;

import mandysax.fragment.FragmentActivity;
import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.ViewModelProviders;
import mandysax.lifecycle.livedata.MutableLiveData;
import studio.mandysa.music.service.model.MusicModel;

public class DefaultPlayerManager extends ViewModel {

    private final MutableLiveData<MusicModel> mPlayingMusic = new MutableLiveData<>();

    /*private final Mu*/

    public DefaultPlayerManager getInstance(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(DefaultPlayerManager.class);
    }
}
