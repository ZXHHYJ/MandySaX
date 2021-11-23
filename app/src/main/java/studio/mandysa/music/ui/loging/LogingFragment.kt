package studio.mandysa.music.ui.loging

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import mandysax.fragment.DialogFragment
import studio.mandysa.music.R
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.ui.event.UserViewModel

class LogingFragment(private val mobilePhone: String, private val password: String) :
    DialogFragment() {
    private val mEvent: UserViewModel by activityViewModels()

    override fun onActivityCreated(bundle: Bundle?) {
        super.onActivityCreated(bundle)
        mEvent.login(mobilePhone, password)
        mEvent.getCookieLiveData().lazy {
            dismiss()
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setCancelable(false)
            .setView(layoutInflater.inflate(R.layout.layout_loading, null))
            .create()
    }


}