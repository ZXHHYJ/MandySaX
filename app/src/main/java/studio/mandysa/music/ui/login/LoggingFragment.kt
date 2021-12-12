package studio.mandysa.music.ui.login

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import mandysax.anna2.Anna2
import mandysax.fragment.DialogFragment
import studio.mandysa.music.R
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.logic.utils.toast
import studio.mandysa.music.ui.event.UserViewModel

class LoggingFragment(private val mobilePhone: String, private val password: String) :
    DialogFragment() {
    private val mEvent: UserViewModel by activityViewModels()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        mEvent.login(mobilePhone, password).lazy {
            when (it) {
                502 -> {
                    toast("手机号或密码错误")
                }
                Anna2.UNKNOWN -> {
                    toast("网络错误")
                }
                else -> {
                    toast("登录成功")
                }
            }
            dismiss()
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setCancelable(false)
            .setView(layoutInflater.inflate(R.layout.layout_loading, null))
            .create().also {
                it.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            }
    }


}