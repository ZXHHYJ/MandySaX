package studio.mandysa.music.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.fragment.Fragment
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentMeBinding
import studio.mandysa.music.databinding.LayoutLoginBinding
import studio.mandysa.music.logic.utils.FragmentUtils.bindView

class MeFragment : Fragment() {

    private val mBinding: FragmentMeBinding by bindView()

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
            statelayout.apply {
                setEmptyLayoutId(R.layout.layout_login)
                showEmpty {
                    LayoutLoginBinding.bind(this).let {
                        mImageLoader.displayImage(
                            "https://cdn.colorhub.me/iE10vz7L36euvhRdZqrIi1YDTCSSl2xDkxI2x6LnhBo/auto/0/500/ce/0/bG9jYWw6Ly8vYTQv/ZDIvMGRmOGQ2Y2Vh/ZWQ2M2U5NTQ2YzYx/YTQ0NjEzMjJmMWJj/YWRkYTRkMi5qcGc.jpg",
                            it.show
                        )
                    }
                }
                showEmptyState()
            }
        }
    }

    override fun onDestroyView() {
        mImageLoader.clearMemoryCache()
        super.onDestroyView()
    }
}