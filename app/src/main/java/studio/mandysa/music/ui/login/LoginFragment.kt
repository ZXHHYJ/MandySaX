package studio.mandysa.music.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import mandysax.fragment.DialogFragment
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.ui.event.UserViewModel

class LoginFragment : DialogFragment() {

    private val mEvent: UserViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle("神灯")
            .setMessage("来选择你要实现的一个愿望把")
            .setPositiveButton(
                "车子"
            ) { _, _ -> }
            .setNegativeButton(
                "房子"
            ) { _, _ -> }.create()
    }
}