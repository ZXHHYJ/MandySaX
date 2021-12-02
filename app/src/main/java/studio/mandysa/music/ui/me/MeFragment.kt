package studio.mandysa.music.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import mandysax.navigation.Navigation
import studio.mandysa.jiuwo.utils.linear
import studio.mandysa.jiuwo.utils.recyclerAdapter
import studio.mandysa.jiuwo.utils.setup
import studio.mandysa.jiuwo.utils.staggered
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentMeBinding
import studio.mandysa.music.databinding.ItemMePlaylistBinding
import studio.mandysa.music.databinding.ItemToplistBinding
import studio.mandysa.music.databinding.ItemUserBinding
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.model.UserModel
import studio.mandysa.music.logic.model.UserPlaylistModel
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.set
import studio.mandysa.music.ui.all.playlist.PlaylistFragment
import studio.mandysa.music.ui.decoration.DoubleItemDecoration
import studio.mandysa.music.ui.event.UserViewModel

class MeFragment : Fragment() {

    private val mBinding: FragmentMeBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    private val mImageLoader = ImageLoader.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            recycler.linear().setup {
                addType<UserModel>(R.layout.item_user)
                addType<UserPlaylistModel>(R.layout.item_me_playlist)
                onBind {
                    when (itemViewType) {
                        R.layout.item_user -> {
                            val model = getModel<UserModel>()
                            ItemUserBinding.bind(itemView).apply {
                                mImageLoader.displayImage(model.avatarUrl, ivAvatar)
                                tvNickname.text = model.nickname
                            }
                        }
                        R.layout.item_me_playlist -> {
                            ItemMePlaylistBinding.bind(itemView).playlistList.apply {
                                addItemDecoration(DoubleItemDecoration())
                                staggered(2).setup {
                                    addType<UserPlaylistModel.UserPlaylist>(R.layout.item_toplist)
                                    onBind {
                                        val model = getModel<UserPlaylistModel.UserPlaylist>()
                                        ItemToplistBinding.bind(itemView).apply {
                                            mImageLoader.displayImage(
                                                model.coverImgUrl,
                                                toplistCover
                                            )
                                            toplistName.text = model.name
                                        }
                                        itemView.setOnClickListener {
                                            Navigation.findViewNavController(it)
                                                .navigate(
                                                    R.style.AppFragmentAnimStyle,
                                                    PlaylistFragment(model.id!!)
                                                )
                                        }
                                    }
                                }
                                recyclerAdapter.addModels(getModel<UserPlaylistModel>().list)
                            }
                        }
                    }
                }
            }
            statelayout.showLoading {
                NeteaseCloudMusicApi::class.java.create()
                    .getUserInfo(mEvent.getCookieLiveData().value, System.currentTimeMillis())
                    .set(viewLifecycleOwner.lifecycle, object : Callback<UserModel> {
                        override fun onFailure(code: Int) {
                            statelayout.showErrorState()
                        }

                        override fun onResponse(t: UserModel?) {
                            recycler.recyclerAdapter.addHeader(t!!)
                            NeteaseCloudMusicApi::class.java.create().getUserPlaylist(t.userId).set(
                                viewLifecycleOwner.lifecycle,
                                object : Callback<UserPlaylistModel> {
                                    override fun onFailure(code: Int) {
                                        statelayout.showErrorState()
                                    }

                                    override fun onResponse(t: UserPlaylistModel?) {
                                        recycler.recyclerAdapter.addModels(listOf(t!!))
                                    }

                                })
                            statelayout.showContentState()
                        }

                    })
            }
            statelayout.showLoadingState()
        }
    }

    override fun onDestroyView() {
        mImageLoader.clearMemoryCache()
        super.onDestroyView()
    }
}