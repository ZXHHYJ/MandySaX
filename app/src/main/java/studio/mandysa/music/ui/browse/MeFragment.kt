package studio.mandysa.music.ui.browse

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.*
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentMeBinding
import studio.mandysa.music.databinding.ItemMePlaylistRvBinding
import studio.mandysa.music.databinding.ItemPlaylistBinding
import studio.mandysa.music.databinding.ItemUserBinding
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylistModel
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.set
import studio.mandysa.music.ui.all.playlist.PlaylistFragment
import studio.mandysa.music.ui.browse.mylike.MyLikeFragment
import studio.mandysa.music.ui.browse.user.UserFragment
import studio.mandysa.music.ui.event.UserViewModel

class MeFragment : Fragment() {

    private val mBinding: FragmentMeBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recycler.linear().setup {
            addType<UserModel>(R.layout.item_user)
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
                    R.layout.item_me_playlist_rv -> {
                        ItemMePlaylistRvBinding.bind(itemView).recycler.apply {
                            linearSnapHelper.attachToRecyclerView(this)
                            addItemDecoration(object : RecyclerView.ItemDecoration() {
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
                            }, 0)
                            linear(orientation = HORIZONTAL).setup {
                                addType<UserPlaylistModel.UserPlaylist>(R.layout.item_playlist)
                                onBind {
                                    val model = getModel<UserPlaylistModel.UserPlaylist>()
                                    ItemPlaylistBinding.bind(itemView).apply {
                                        playlistCover.setImageURI(model.coverImgUrl)
                                        playlistTitle.text = model.name
                                        /*authorName.text = "by ${model.nickname}"*/
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
                            it.recycler.addHeader(t!!)
                            NeteaseCloudMusicApi::class.java.create().getUserPlaylist(t.userId).set(
                                viewLifecycleOwner.lifecycle,
                                object : Callback<UserPlaylistModel> {
                                    override fun onFailure(code: Int) {
                                        it.stateLayout.showErrorState()
                                    }

                                    override fun onResponse(t: UserPlaylistModel?) {
                                        it.recycler.addModels(listOf(t!!))
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