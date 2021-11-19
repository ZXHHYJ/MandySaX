package studio.mandysa.music.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.nostra13.universalimageloader.core.ImageLoader
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import mandysax.anna2.callback.Callback
import mandysax.core.app.OnBackPressedCallback
import mandysax.lifecycle.Lifecycle
import mandysax.lifecycle.ViewModelProviders
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.recyclerAdapter
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.setup
import studio.mandysa.jiuwo.utils.RecyclerViewUtils.staggered
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentSearchBinding
import studio.mandysa.music.databinding.ItemToplistBinding
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.ToplistModel
import studio.mandysa.music.logic.utils.ClassUtils.create
import studio.mandysa.music.logic.utils.EditTextUtils.hideInput
import studio.mandysa.music.logic.utils.ObservableUtils.set
import studio.mandysa.music.ui.all.playlist.PlaylistFragment
import studio.mandysa.music.ui.base.BaseFragment

class SearchFragment : BaseFragment() {

    private val mBinding: FragmentSearchBinding by bindView()

    private val mImageLoader = ImageLoader.getInstance()

    private var mViewModel: SearchViewModel? = null

    private val mOnBackListener = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (mBinding.searchSlidingView.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                mBinding.searchSlidingView.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = ViewModelProviders.of(activity)[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.searchSlidingView.addPanelSlideListener(object :
            SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {

            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                mOnBackListener.isEnabled = newState == SlidingUpPanelLayout.PanelState.EXPANDED
            }

        })
        viewLifecycleOwner.lifecycle.addObserver { state ->
            when (state) {
                Lifecycle.Event.ON_DESTROY -> {
                    mOnBackListener.remove()
                }
                Lifecycle.Event.ON_CREATE -> {
                    activity.onBackPressedDispatcher.addCallback(mOnBackListener)
                }
                Lifecycle.Event.ON_START -> {
                    mOnBackListener.isEnabled =
                        mBinding.searchSlidingView.panelState == SlidingUpPanelLayout.PanelState.EXPANDED
                }
                Lifecycle.Event.ON_STOP -> {
                    mOnBackListener.isEnabled = false
                }
                else -> {}
            }
        }
        mBinding.apply {
            statelayout.apply {
                showLoading {
                    NeteaseCloudMusicApi::class.java.create().getToplist()
                        .set(lifecycle, object : Callback<List<ToplistModel>> {
                            override fun onResponse(t: List<ToplistModel>?) {
                                mBinding.recycler.recyclerAdapter.models = t!!
                                showContentState()
                            }

                            override fun onFailure(code: Int) {
                                showErrorState()
                            }

                        })
                }
                showLoadingState()
            }
            searchSlidingView.isTouchEnabled = false
            searchEditFrame.setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    hideInput()
                    searchSlidingView.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }
                false
            }
            searchEditFrame.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    mViewModel!!.searchContentLiveData.value = s.toString()
                    searchSlidingView.panelState =
                        if (TextUtils.isEmpty(s)) SlidingUpPanelLayout.PanelState.COLLAPSED else SlidingUpPanelLayout.PanelState.EXPANDED
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
            recycler.staggered(2).setup {
                addType<ToplistModel>(R.layout.item_toplist)
                onBind {
                    val model = getModel<ToplistModel>()
                    ItemToplistBinding.bind(itemView).apply {
                        mImageLoader.displayImage(model.coverImgUrl, toplistCover)
                        toplistName.text = model.name
                        toplistCover.setOnClickListener {
                            Navigation.findViewNavController(it)
                                .navigate(
                                    R.style.test,
                                    PlaylistFragment(model.id!!)
                                )
                        }
                    }
                }
            }
        }

    }

    private fun hideInput() {
        if (mBinding.searchEditFrame.isFocused) {
            mBinding.searchEditFrame.clearFocus()
            mBinding.searchEditFrame.hideInput()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel = null
        mImageLoader.clearMemoryCache()
    }

}