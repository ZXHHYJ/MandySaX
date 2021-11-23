package studio.mandysa.music.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.WindowManager
import mandysax.fragment.DialogFragment
import studio.mandysa.music.R
import studio.mandysa.music.databinding.DialogLoginBinding
import studio.mandysa.music.ui.loging.LogingFragment


class LoginFragment : DialogFragment() {

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    private var mBinding: DialogLoginBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mBinding = DialogLoginBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireActivity(), R.style.MyDialog)
            .setTitle(context.getString(R.string.login))
            .setCancelable(false)
            .setView(mBinding!!.root)
            .setPositiveButton(
                context.getString(R.string.login)
            ) { _, _ ->
                LogingFragment(
                    mBinding!!.etPhone.text.toString(),
                    mBinding!!.etPassword.text.toString()
                ).show(requireActivity().fragmentPlusManager)
            }
            .setNegativeButton(
                context.getString(R.string.cancel)
            ) { _, _ -> }.create().also {
                it.setOnShowListener { _ ->
                    val layoutParams: WindowManager.LayoutParams =
                        it.window!!.attributes
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    it.window!!.decorView.setPadding(0, 0, 0, 0)
                    it.window!!.attributes = layoutParams
                    it.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    createTextChangedListener(it).apply {
                        mBinding!!.etPhone.addTextChangedListener(this)
                        mBinding!!.etPassword.addTextChangedListener(this)
                    }

                }
            }
    }

    private fun createTextChangedListener(alertDialog: AlertDialog) = object : TextWatcher {
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
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                mBinding!!.etPhone.text.length == 11 && mBinding!!.etPassword.text.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

}