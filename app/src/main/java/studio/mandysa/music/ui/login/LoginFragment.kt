package studio.mandysa.music.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import mandysax.fragment.DialogFragment
import mandysax.lifecycle.LifecycleObserver
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentLoginBinding
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.ui.event.UserViewModel

class LoginFragment : DialogFragment(), LifecycleObserver {

    private val mEvent: UserViewModel by activityViewModels()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        mEvent.getCookieLiveData().lazy {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity(), R.style.TransparentDialog)
            .setCancelable(false)
            .create().also { it ->
                val binding = FragmentLoginBinding.inflate(
                    layoutInflater,
                    it.window!!.decorView as ViewGroup,
                    true
                )
                binding.let {
                    it.btnLogin.setOnClickListener {
                        LoggingFragment(
                            binding.etPhone.text.toString(),
                            binding.etPassword.text.toString()
                        ).show(requireActivity().fragmentPlusManager)
                    }
                    it.btnLogin.isEnabled = false
                    createTextChangedListener(it).apply {
                        it.etPhone.addTextChangedListener(this)
                        it.etPassword.addTextChangedListener(this)
                    }
                }
                it.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                it.window!!.setGravity(Gravity.BOTTOM)
                it.setOnShowListener { _ ->
                    it.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                }
            }
    }

    private fun createTextChangedListener(binding: FragmentLoginBinding) = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {

        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            binding.let {
                it.btnLogin.isEnabled =
                    it.etPhone.text.length == 11 && it.etPassword.text.isNotEmpty()
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

}