package studio.mandysa.music.ui.search

import mandysax.lifecycle.ViewModel
import mandysax.lifecycle.livedata.MutableLiveData

class SearchViewModel : ViewModel() {
    val searchContentLiveData = MutableLiveData<String>()
}