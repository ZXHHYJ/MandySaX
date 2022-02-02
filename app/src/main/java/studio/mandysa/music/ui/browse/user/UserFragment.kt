package studio.mandysa.music.ui.browse.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mandysax.fragment.Fragment
import studio.mandysa.music.databinding.FragmentUserBinding
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.logic.utils.bindView
import studio.mandysa.music.ui.event.UserViewModel

class UserFragment : Fragment() {
    private val mBinding: FragmentUserBinding by bindView()

    private val mEvent: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}