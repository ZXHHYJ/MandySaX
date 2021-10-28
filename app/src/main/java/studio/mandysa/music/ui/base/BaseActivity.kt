package studio.mandysa.music.ui.base

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import mandysax.fragment.FragmentActivity
import mandysax.lifecycle.*

open class BaseActivity : FragmentActivity() {
    inline fun <reified VB : ViewBinding> Activity.inflate() = lazy {
        inflateBinding<VB>(layoutInflater)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
        VB::class.java.getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as VB

    inline fun <reified VB : ViewModel> ViewModelStoreOwner.viewModels() = lazy {
        return@lazy ViewModelProviders.of(
            this
        )[VB::class.java]
    }

}