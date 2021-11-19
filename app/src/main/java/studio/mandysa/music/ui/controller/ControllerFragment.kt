package studio.mandysa.music.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.fragment.Fragment
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentControllerBinding
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.getInstance

class ControllerFragment : Fragment() {

    private val mBinding: FragmentControllerBinding by bindView()

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
        getInstance().changeMusicLiveData().observe(viewLifecycleOwner) {
            mBinding.apply {
                controllerTitle.text = it.title
                mImageLoader.displayImage(it.coverUrl, controllerCover)
            }
        }
        getInstance().pauseLiveData().observe(viewLifecycleOwner) {
            mBinding.controllerPlayOrPause.setImageResource(if (it) R.drawable.ic_play else R.drawable.ic_pause)
        }
        mBinding.controllerPlayOrPause.setOnClickListener {
            getInstance().apply {
                if (pauseLiveData().value == true)
                    play()
                else pause()
            }
        }
        mBinding.controllerNextSong.setOnClickListener {
            getInstance().skipToNext()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mImageLoader.clearMemoryCache()
    }

}