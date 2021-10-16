package studio.mandysa.music.ui.event;

import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.livedata.MutableLiveData;

public class ShareViewModel extends ViewModel {

    public final MutableLiveData<Integer> homePosLiveData =new MutableLiveData<>(0);

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
