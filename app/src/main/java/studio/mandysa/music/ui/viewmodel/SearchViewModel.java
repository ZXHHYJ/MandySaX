package studio.mandysa.music.ui.viewmodel;

import mandysax.lifecycle.ViewModel;
import mandysax.lifecycle.livedata.LiveData;
import mandysax.lifecycle.livedata.MutableLiveData;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mSearchContent = new MutableLiveData<>();

    public void searchFor(String text) {
        mSearchContent.setValue(text);
    }

    public LiveData<String> getSearchContent() {
        return mSearchContent;
    }

}
