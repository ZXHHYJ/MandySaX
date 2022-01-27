package studio.mandysa.music.ui.me

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.navigation.Navigation
import mandysax.tablayout.NavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.jiuwo.utils.staggered
import studio.mandysa.music.R
import studio.mandysa.music.databinding.*
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylistModel
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.set
import studio.mandysa.music.ui.all.playlist.PlaylistFragment
import studio.mandysa.music.ui.event.UserViewModel
import studio.mandysa.music.ui.me.mylike.MyLikeFragment
import studio.mandysa.music.ui.search.SearchFragment

class MeFragment : Fragment() {

    private val mBinding: FragmentMeBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LayoutToolbarBinding.bind(mBinding.root).apply {
            editFrame.let {
                it.isFocusableInTouchMode = false//不可编辑
                it.keyListener = null//不可粘贴，长按不会弹出粘贴框
                //it.setClickable(false);//不可点击，但是这个效果我这边没体现出来，不知道怎没用
                it.isFocusable = false//不可编辑
            }
            editFrame.setOnClickListener {
                Navigation.findViewNavController(it).navigate(SearchFragment())
            }
            topNav.models = listOf(
                NavigationItem(
                    "我的"
                )
            ).setInActiveColor(context.getColor(android.R.color.black))
                .setActiveColor(context.getColor(R.color.main))
            topNav.setSelectedPosition(0)
        }
        mBinding.recycler.linear().setup {
            addType<UserModel>(R.layout.item_user_head)
            addType<UserPlaylistModel>(R.layout.item_me_playlist_rv)
            val pagerSnapHelper = PagerSnapHelper()
            onBind {
                when (itemViewType) {
                    R.layout.item_user_head -> {
                        val model = getModel<UserModel>()
                        ItemUserHeadBinding.bind(itemView).apply {
                            ivAvatar.setImageURI(model.avatarUrl)
                            ivAvatar
                            tvNickname.text = model.nickname
                        }
                    }
                    R.layout.item_me_playlist_rv -> {
                        ItemMePlaylistRvBinding.bind(itemView).playlistList.apply {
                            pagerSnapHelper.attachToRecyclerView(this)
                            staggered(3, orientation = HORIZONTAL).setup {
                                addType<UserPlaylistModel.UserPlaylist>(R.layout.item_my_playlist)
                                onBind {
                                    val model = getModel<UserPlaylistModel.UserPlaylist>()
                                    ItemMyPlaylistBinding.bind(itemView).apply {
                                        playlistCover.setImageURI(model.coverImgUrl)
                                        playlistName.text = model.name
                                        authorName.text = "by ${model.nickname}"
                                    }
                                    itemView.setOnClickListener {
                                        Navigation.findViewNavController(it)
                                            .navigate(
                                                R.style.AppFragmentAnimStyle,
                                                if (modelPosition == 0) MyLikeFragment(model.id!!) else PlaylistFragment(
                                                    model.id!!
                                                )
                                            )
                                    }
                                }
                            }
                            recyclerAdapter.models = getModel<UserPlaylistModel>().list
                        }
                    }
                }
            }
        }

        mBinding.let {
            it.stateLayout.showLoading {
                NeteaseCloudMusicApi::class.java.create()
                    .getUserInfo(mEvent.getCookieLiveData().value, System.currentTimeMillis())
                    .set(viewLifecycleOwner.lifecycle, object : Callback<UserModel> {
                        override fun onFailure(code: Int) {
                            it.stateLayout.showErrorState()
                        }

                        override fun onResponse(t: UserModel?) {
                            val list = ArrayList<Any>()
                            list.add(t!!)
                            NeteaseCloudMusicApi::class.java.create().getUserPlaylist(t.userId).set(
                                viewLifecycleOwner.lifecycle,
                                object : Callback<UserPlaylistModel> {
                                    override fun onFailure(code: Int) {
                                        it.stateLayout.showErrorState()
                                    }

                                    override fun onResponse(t: UserPlaylistModel?) {
                                        list.add(t!!)
                                        it.recycler.recyclerAdapter.models = list
                                    }

                                })
                            it.stateLayout.showContentState()
                        }

                    })
            }
            it.stateLayout.showLoadingState()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

}