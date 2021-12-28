package studio.mandysa.music.ui.search

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.sothree.slidinguppanel.PanelSlideListener
import com.sothree.slidinguppanel.PanelState
import mandysax.anna2.callback.Callback
import mandysax.core.app.OnBackPressedCallback
import mandysax.fragment.Fragment
import mandysax.lifecycle.Lifecycle
import mandysax.lifecycle.livedata.Observer
import mandysax.media.DefaultPlayerManager
import mandysax.media.model.DefaultArtist
import mandysax.media.model.DefaultMusic
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentSearchBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.databinding.ItemToplistBinding
import studio.mandysa.music.databinding.ItemToplistsBinding
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.NewSongModel
import studio.mandysa.music.logic.model.ToplistsModel
import studio.mandysa.music.logic.utils.*
import studio.mandysa.music.ui.all.playlist.PlaylistFragment

class SearchFragment : Fragment() {

    private val mBinding: FragmentSearchBinding by bindView()

    private val mViewModel: SearchViewModel by activityViewModels()

    private val mOnBackListener = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (mBinding.searchSlidingView.panelState == PanelState.EXPANDED) {
                mBinding.searchSlidingView.panelState = PanelState.COLLAPSED
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.searchSlidingView.addPanelSlideListener(object :
            PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {

            }

            override fun onPanelStateChanged(
                panel: View,
                previousState: PanelState,
                newState: PanelState
            ) {
                mOnBackListener.isEnabled = newState == PanelState.EXPANDED
            }

        })
        viewLifecycleOwner.lifecycle.addObserver { state ->
            when (state) {
                Lifecycle.Event.ON_DESTROY -> {
                    mOnBackListener.remove()
                }
                Lifecycle.Event.ON_CREATE -> {
                    requireActivity().onBackPressedDispatcher.addCallback(mOnBackListener)
                }
                Lifecycle.Event.ON_START -> {
                    mOnBackListener.isEnabled =
                        mBinding.searchSlidingView.panelState == PanelState.EXPANDED
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
                    NeteaseCloudMusicApi::class.java.create().apply {
                        getToplist()
                            .set(viewLifecycleOwner.lifecycle, object : Callback<ToplistsModel> {
                                override fun onResponse(t: ToplistsModel?) {
                                    mBinding.recycler.recyclerAdapter.models = listOf(t)
                                    showContentState()
                                }

                                override fun onFailure(code: Int) {
                                    showErrorState()
                                }

                            })
                        getRecommendedNewSong().set(
                            viewLifecycleOwner.lifecycle,
                            object : Callback<List<NewSongModel>> {
                                override fun onResponse(t: List<NewSongModel>?) {
                                    statelayout.showContentState()
                                    mBinding.recycler.recyclerAdapter.footers = t
                                }

                                override fun onFailure(code: Int) {
                                    statelayout.showErrorState()
                                }

                            })
                    }
                }
                showLoadingState()
            }
            searchSlidingView.isTouchEnabled = false
            searchEditFrame.setOnEditorActionListener { v, i, _ ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    hideInput()
                    mViewModel.searchContentLiveData.value = v.text.toString()
                    searchSlidingView.panelState = PanelState.EXPANDED
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
                    searchSlidingView.panelState =
                        if (TextUtils.isEmpty(s)) PanelState.COLLAPSED else PanelState.EXPANDED
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
            recycler.linear().setup {
                addType<ToplistsModel>(R.layout.item_toplists)
                addType<NewSongModel>(R.layout.item_song)
                val snapHelper = LinearSnapHelper()
                onBind {
                    when (itemViewType) {
                        R.layout.item_toplists -> {
                            ItemToplistsBinding.bind(itemView).apply {
                                snapHelper.attachToRecyclerView(recycler)
                                recycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
                                    override fun getItemOffsets(
                                        outRect: Rect,
                                        view: View,
                                        parent: RecyclerView,
                                        state: RecyclerView.State
                                    ) {
                                        val length =
                                            parent.resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin)
                                        view.layoutParams.width =
                                            parent.resources.getDimensionPixelOffset(R.dimen.album_width) + length / 2
                                        view.setPadding(length, 0, 0, 0)
                                    }
                                })
                                recycler.linear(orientation = HORIZONTAL).setup {
                                    addType<ToplistsModel.ToplistModel>(R.layout.item_toplist)
                                    onBind {
                                        val model = getModel<ToplistsModel.ToplistModel>()
                                        ItemToplistBinding.bind(itemView).apply {
                                            toplistCover.setImageURI(model.coverImgUrl)
                                            toplistName.text = model.name
                                            itemView.setOnClickListener {
                                                Navigation.findViewNavController(it)
                                                    .navigate(
                                                        R.style.AppFragmentAnimStyle,
                                                        PlaylistFragment(model.id!!)
                                                    )
                                            }
                                        }
                                    }
                                }
                                recycler.recyclerAdapter.models = getModel<ToplistsModel>().list
                            }
                        }
                        R.layout.item_song -> {
                            val model = getModel<NewSongModel>()
                            ItemSongBinding.bind(itemView).apply {
                                songName.text = model.title
                                songSingerName.text = model.artist[0].name
                                songCover.setImageURI(model.coverUrl)
                                itemView.setOnClickListener {
                                    DefaultPlayerManager.getInstance()!!.apply {
                                        loadAlbum(
                                            models.createAlbum(),
                                            modelPosition
                                        )
                                        play()
                                    }
                                }
                                val lifecycleObserver =
                                    Observer<DefaultMusic<DefaultArtist>> { p1 ->
                                        if (p1.id == model.id) {
                                            songName.setTextColor(
                                                context.getColor(R.color.blue)
                                            )
                                            songSingerName.setTextColor(context.getColor(R.color.blue))
                                        } else {
                                            songName.setTextColor(
                                                context.getColor(android.R.color.black)
                                            )
                                            songSingerName.setTextColor(context.getColor(R.color.tv_color_light))
                                        }
                                    }
                                onAttached {
                                    DefaultPlayerManager.getInstance()!!.changeMusicLiveData()
                                        .observe(viewLifecycleOwner, lifecycleObserver)
                                }
                                onDetached {
                                    DefaultPlayerManager.getInstance()!!.changeMusicLiveData()
                                        .removeObserver(lifecycleObserver)
                                }
                            }
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

}