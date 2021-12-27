package studio.mandysa.music.ui.play

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import mandysax.fragment.Fragment
import mandysax.fragmentpage.widget.FragmentPage
import studio.mandysa.bottomnavigationbar.BottomNavigationItem
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlayBinding
import studio.mandysa.music.logic.utils.BitmapUtil
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.getFrescoCacheBitmap
import studio.mandysa.music.logic.utils.getInstance


class PlayFragment : Fragment() {

    private val mBinding: FragmentPlayBinding by bindView()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instance = getInstance()
        ViewCompat.setOnApplyWindowInsetsListener(
            view
        ) { _, insets ->
            val startBarSize = insets!!.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarSize =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            mBinding.playLayout.setPadding(0, startBarSize, 0, navigationBarSize)
            insets
        }
        mBinding.let { it ->
            it.fragmentPage.setAdapter(object : FragmentPage.Adapter {
                override fun onCreateFragment(position: Int): Fragment? = when (position) {
                    0 -> PlayingFragment()
                    1 -> LyricFragment()
                    2 -> PlayQueueFragment()
                    else -> null
                }
            })
            it.bottomNavigationBar.setInActiveColorResource(R.color.translucent_white)
            it.bottomNavigationBar.setActiveColorResource(android.R.color.white)
            it.bottomNavigationBar.models = listOf(
                BottomNavigationItem(
                    R.drawable.ic_round_music_video_24,
                    ""
                ),
                BottomNavigationItem(
                    R.drawable.ic_round_font_download_24,
                    ""
                ), BottomNavigationItem(
                    R.drawable.ic_round_format_list_bulleted_24,
                    ""
                )
            )
            it.bottomNavigationBar.getSelectedPosition().observe(viewLifecycleOwner) {
                mBinding.fragmentPage.position = it
            }
            it.bottomNavigationBar.setSelectedPosition(0)
            instance.pauseLiveData().observe(viewLifecycleOwner) { pause ->
                if (pause) {
                    it.playBackground.pause()
                } else {
                    it.playBackground.resume()
                }
            }
            /**
             * 更新背景图片
             */
            instance.changeMusicLiveData().observe(viewLifecycleOwner) { model ->
                getFrescoCacheBitmap(context, model.coverUrl, object : BaseBitmapDataSubscriber() {
                    override fun onNewResultImpl(bitmap: Bitmap?) {
                        val blurBitmap = BitmapUtil.handleImageBlur(context, bitmap!!)
                        mBinding.playBackground.setImageBitmap(blurBitmap)
                    }

                    override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {

                    }
                })
            }
            /**
             * 背景模糊动画速度
             */
            it.playBackground.setTransitionGenerator(RandomTransitionGenerator().also {
                it.setTransitionDuration(3000)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (getInstance().pauseLiveData().value == false)
            mBinding.playBackground.resume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.playBackground.pause()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

}