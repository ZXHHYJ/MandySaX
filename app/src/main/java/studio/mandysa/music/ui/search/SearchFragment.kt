package studio.mandysa.music.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.anna2.callback.Callback
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
import studio.mandysa.music.ui.base.BaseFragment
import studio.mandysa.music.ui.searchlist.SearchListFragment


class SearchFragment : BaseFragment() {

    private val mBinding: FragmentSearchBinding by bindView()

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
        val data = arrayOf(
            "小猪猪", "小狗狗", "小鸡鸡", "小猫猫", "小咪咪"
        )
        mBinding.apply {
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, data)
            searchEditFrame.setAdapter(adapter)
            searchEditFrame.setOnEditorActionListener { _, i, _ ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    hideInput()
                    Navigation.findViewNavController(view)
                        .navigate(R.style.test, SearchListFragment())
                }
                false
            }
            recycler.staggered(2).setup {
                addType<ToplistModel>(R.layout.item_toplist)
                onBind {
                    val model = getModel<ToplistModel>()
                    ItemToplistBinding.bind(itemView).apply {
                        mImageLoader.displayImage(model.coverImgUrl, toplistCover)
                        toplistName.text = model.name
                    }
                }
            }
        }
        NeteaseCloudMusicApi::class.java.create().getToplist()
            .set(lifecycle, object : Callback<List<ToplistModel>> {
                override fun onResponse(t: List<ToplistModel>?) {
                    /*statelayout.showContentState()*/
                    mBinding.recycler.recyclerAdapter.models = t!!
                }

                override fun onFailure(code: Int) {
                    /*statelayout.showErrorState()*/
                }

            })
    }

    private fun hideInput() {
        if (mBinding.searchEditFrame.isFocused) {
            mBinding.searchEditFrame.clearFocus()
            mBinding.searchEditFrame.hideInput()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mImageLoader.clearMemoryCache()
    }

}