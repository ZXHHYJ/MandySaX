package studio.mandysa.music.ui.me

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
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
import studio.mandysa.music.databinding.ItemMyPlaylistBinding
import studio.mandysa.music.databinding.ItemUserHeadBinding
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

class MeFragment : Fragment() {

    private val mBinding: FragmentMeBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    private val mImageLoader = ImageLoader.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.recycler.linear().setup {
            addType<UserModel>(R.layout.item_user_head)
            addType<UserPlaylistModel>(R.layout.item_me_playlist)
            val snapHelper = PagerSnapHelper()
            onBind {
                when (itemViewType) {
                    R.layout.item_user_head -> {
                        val model = getModel<UserModel>()
                        ItemUserHeadBinding.bind(itemView).apply {
                            mImageLoader.displayImage(model.avatarUrl, ivAvatar)
                            tvNickname.text = model.nickname
                        }
                    }
                    R.layout.item_me_playlist -> {
                        ItemMePlaylistBinding.bind(itemView).playlistList.apply {
                            snapHelper.attachToRecyclerView(this)
                            staggered(3, orientation = HORIZONTAL).setup {
                                addType<UserPlaylistModel.UserPlaylist>(R.layout.item_my_playlist)
                                onBind {
                                    val model = getModel<UserPlaylistModel.UserPlaylist>()
                                    ItemMyPlaylistBinding.bind(itemView).apply {
                                        mImageLoader.displayImage(
                                            model.coverImgUrl,
                                            playlistCover
                                        )
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
            it.statelayout.showLoading {
                NeteaseCloudMusicApi::class.java.create()
                    .getUserInfo(mEvent.getCookieLiveData().value, System.currentTimeMillis())
                    .set(viewLifecycleOwner.lifecycle, object : Callback<UserModel> {
                        override fun onFailure(code: Int) {
                            it.statelayout.showErrorState()
                        }

                        override fun onResponse(t: UserModel?) {
                            val list = ArrayList<Any>()
                            list.add(t!!)
                            NeteaseCloudMusicApi::class.java.create().getUserPlaylist(t.userId).set(
                                viewLifecycleOwner.lifecycle,
                                object : Callback<UserPlaylistModel> {
                                    override fun onFailure(code: Int) {
                                        it.statelayout.showErrorState()
                                    }

                                    override fun onResponse(t: UserPlaylistModel?) {
                                        list.add(t!!)
                                        it.recycler.recyclerAdapter.models = list
                                    }

                                })
                            it.statelayout.showContentState()
                        }

                    })
            }
            it.statelayout.showLoadingState()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onDestroyView() {
        mImageLoader.clearMemoryCache()
        super.onDestroyView()
    }
}