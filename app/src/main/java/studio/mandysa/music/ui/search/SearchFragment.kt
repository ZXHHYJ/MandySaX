package studio.mandysa.music.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentSearchBinding
import studio.mandysa.music.logic.utils.bindView

class SearchFragment : Fragment() {

    private val mBinding: FragmentSearchBinding by bindView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
          searchEditFrame.setOnEditorActionListener { v, i, _ ->
              if (i == EditorInfo.IME_ACTION_SEARCH) {
                  hideInput()
                  mViewModel.searchContentLiveData.value = v.text.toString()
                  searchSlidingView.panelState = PanelState.EXPANDED
              }
              false
          }*/
        /* searchEditFrame.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(
                 s: CharSequence?,
                 start: Int,
                 count: Int,
                 after: Int
             ) {

             }

             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                 searchSlidingView.panelState =
                     if (TextUtils.isEmpty(s)) PanelState.COLLAPSED else PanelState.EXPANDED
             }

             override fun afterTextChanged(s: Editable?) {

             }

         })*/

    }

}

/* private fun hideInput() {
     if (mBinding.searchEditFrame.isFocused) {
         mBinding.searchEditFrame.clearFocus()
         mBinding.searchEditFrame.hideInput()
     }
 }*/

