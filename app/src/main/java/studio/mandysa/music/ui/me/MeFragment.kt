package studio.mandysa.music.ui.me

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.navigation.Navigation
import mandysax.tablayout.NavigationItem
import mandysax.tablayout.setActiveColor
import mandysax.tablayout.setInActiveColor
import studio.mandysa.jiuwo.utils.*
import studio.mandysa.music.R
import studio.mandysa.music.databinding.*
import studio.mandysa.music.logic.decoration.AlbumItemDecoration
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.UserDetailModel
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylistModel
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.set
import studio.mandysa.music.ui.all.playlist.PlaylistFragment
import studio.mandysa.music.ui.event.UserViewModel
import studio.mandysa.music.ui.me.user.UserFragment
import studio.mandysa.music.ui.search.SearchFragment


class MeFragment : Fragment() {

    private val mBinding: FragmentMeBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LayoutToolbarBinding.bind(mBinding.root).apply {
            editFrame.let {
                it.isFocusableInTouchMode = false
                it.keyListener = null
                it.isFocusable = false
            }
            editFrame.setOnClickListener {
                Navigation.findViewNavController(it).navigate(SearchFragment())
            }
            topNav.models = listOf(
                NavigationItem(
                    context.getString(R.string.me)
                )
            ).setInActiveColor(context.getColor(android.R.color.black))
                .setActiveColor(context.getColor(R.color.theme_color))
            topNav.setSelectedPosition(0)
        }
        mBinding.recycler.linear().setup {
            addType<UserModel>(R.layout.item_user)
            addType<UserDetailModel>(R.layout.item_user_detail)
            addType<UserPlaylistModel>(R.layout.item_me_playlist_rv)
            val linearSnapHelper = LinearSnapHelper()
            onBind {
                when (itemViewType) {
                    R.layout.item_user -> {
                        val model = getModel<UserModel>()
                        ItemUserBinding.bind(itemView).apply {
                            ivAvatar.setImageURI(model.avatarUrl)
                            tvNickname.text = model.nickname
                            tvSignature.text = model.signature
                            itemView.setOnClickListener {
                                Navigation.findViewNavController(it)
                                    .navigate(R.style.AppFragmentAnimStyle, UserFragment())
                            }
                        }
                    }
                    R.layout.item_user_detail -> {
                        fun createSp(top: String, bottom: String): SpannableStringBuilder {
                            val spannableString = SpannableStringBuilder()
                                .append(top)
                                .append("\n$bottom")
                            spannableString.setSpan(
                                ForegroundColorSpan(
                                    ContextCompat.getColor(
                                        context,
                                        android.R.color.black
                                    )
                                ),
                                0,
                                top.length,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                            )
                            spannableString.setSpan(
                                AbsoluteSizeSpan(17, true),
                                0,
                                top.length,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                            )
                            spannableString.setSpan(
                                StyleSpan(Typeface.BOLD),
                                0,
                                top.length,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                            )
                            spannableString.setSpan(
                                ForegroundColorSpan(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.tv_color_light
                                    )
                                ),
                                top.length,
                                top.length + bottom.length,
                                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                            )
                            return spannableString
                        }

                        val model = getModel<UserDetailModel>()
                        ItemUserDetailBinding.bind(itemView).let {
                            it.vip.text = createSp((model.vipType == 11).toString(), "VIP")
                            it.follows.text = createSp(model.follows.toString(), "关注")
                            it.fans.text = createSp(model.fans.toString(), "粉丝")
                        }
                    }
                    R.layout.item_me_playlist_rv -> {
                        ItemMePlaylistRvBinding.bind(itemView).recycler.apply {
                            linearSnapHelper.attachToRecyclerView(this)
                            addItemDecoration(AlbumItemDecoration(), 0)
                            linear(orientation = HORIZONTAL).setup {
                                addType<UserPlaylistModel.UserPlaylist>(R.layout.item_playlist)
                                onBind {
                                    val model = getModel<UserPlaylistModel.UserPlaylist>()
                                    ItemPlaylistBinding.bind(itemView).apply {
                                        playlistCover.setImageURI(model.coverImgUrl)
                                        playlistTitle.text = model.name
                                        itemView.setOnClickListener {
                                            Navigation.findViewNavController(it)
                                                .navigate(
                                                    R.style.AppFragmentAnimStyle,
                                                    PlaylistFragment(
                                                        model.id!!
                                                    )
                                                )
                                        }
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
                NeteaseCloudMusicApi::class.java.create().apply {
                    getUserInfo(mEvent.getCookieLiveData().value, System.currentTimeMillis())
                        .set(viewLifecycleOwner.lifecycle, object : Callback<UserModel> {
                            override fun onFailure(code: Int) {
                                it.stateLayout.showErrorState()
                            }

                            override fun onResponse(t: UserModel?) {
                                it.recycler.addHeader(t!!)
                                getUserDetail(t.userId).set(viewLifecycleOwner.lifecycle,
                                    object : Callback<UserDetailModel> {
                                        override fun onFailure(code: Int) {
                                            it.stateLayout.showErrorState()
                                        }

                                        override fun onResponse(t: UserDetailModel?) {
                                            it.recycler.addModels(listOf(t!!))
                                        }
                                    })
                                getUserPlaylist(t.userId).set(
                                    viewLifecycleOwner.lifecycle,
                                    object : Callback<UserPlaylistModel> {
                                        override fun onFailure(code: Int) {
                                            it.stateLayout.showErrorState()
                                        }

                                        override fun onResponse(t: UserPlaylistModel?) {
                                            it.recycler.addFooter(t!!)
                                        }

                                    })
                                it.stateLayout.showContentState()
                            }

                        })
                }
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