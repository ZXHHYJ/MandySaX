package studio.mandysa.music.ui.start

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.WindowManager
import com.nostra13.universalimageloader.core.ImageLoader
import mandysax.fragment.DialogFragment
import mandysax.lifecycle.LifecycleObserver
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentStartBinding
import studio.mandysa.music.logic.utils.activityViewModels
import studio.mandysa.music.ui.event.UserViewModel
import studio.mandysa.music.ui.login.LoginFragment


class StartFragment : DialogFragment(), LifecycleObserver {

    private val mImageLoader = ImageLoader.getInstance()

    private val mEvent: UserViewModel by activityViewModels()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        mEvent.getCookieLiveData().lazy {
         dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireActivity(), R.style.AppDialog)
            .setCancelable(false)
            .create().also { it ->
                val binding = FragmentStartBinding.inflate(
                    layoutInflater,
                    it.window!!.decorView as ViewGroup,
                    true
                )
                binding.let {
                    it.btnLogin.setOnClickListener {
                        LoginFragment(
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
                it.window!!.setBackgroundDrawableResource(android.R.color.white)
                it.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                it.setOnShowListener { _ ->
                    it.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                }
            }
    }

    private fun createTextChangedListener(binding: FragmentStartBinding) = object : TextWatcher {
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

    override fun onDestroyView() {
        mImageLoader.clearMemoryCache()
        super.onDestroyView()
    }
}