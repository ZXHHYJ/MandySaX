package studio.mandysa.music.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import mandysax.fragment.DialogFragment
import studio.mandysa.music.R
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.ui.event.UserViewModel

class LoginFragment : DialogFragment() {

    private val mEvent: UserViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setTitle("神灯")
            .setView(R.layout.dialog_login)
            .setPositiveButton(
                "车子"
            ) { _, _ -> }
            .setNegativeButton(
                "房子"
            ) { _, _ -> }.create()
    }
}