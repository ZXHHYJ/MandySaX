package studio.mandysa.music.ui.base

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import mandysax.fragment.FragmentActivity

open class BaseActivity : FragmentActivity() {
    inline fun <reified VB : ViewBinding> Activity.inflate() = lazy {
        inflateBinding<VB>(layoutInflater)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
        VB::class.java.getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as VB
}