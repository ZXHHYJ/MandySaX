package studio.mandysa.music.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.anna2.callback.Callback
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentLyricBinding
import studio.mandysa.music.databinding.LayoutPlayToolbarBinding
import studio.mandysa.music.logic.model.LyricModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.logic.utils.create
import studio.mandysa.music.logic.utils.getInstance
import studio.mandysa.music.logic.utils.set

class LyricFragment : Fragment() {

    private val mBinding: FragmentLyricBinding by bindView()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = LayoutPlayToolbarBinding.bind(mBinding.root)
        getInstance().changeMusicLiveData().observe(viewLifecycleOwner) { model ->
            toolbar.songName.text = model.title
            toolbar.songSingerName.text = model.artist[0].name
            toolbar.songCover.setImageURI(model.coverUrl)
            NeteaseCloudMusicApi::class.java.create().getLyric(model.id)
                .set(viewLifecycleOwner.lifecycle, object :
                    Callback<LyricModel> {
                    override fun onFailure(code: Int) {

                    }

                    override fun onResponse(t: LyricModel?) {
                        mBinding.lyricTv.text = t?.lyric
                    }

                })
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