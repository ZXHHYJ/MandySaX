package studio.mandysa.music.logic.utils

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import mandysax.fragment.FragmentActivity

inline fun <reified VB : ViewBinding> FragmentActivity.inflate() = lazy {
    inflateBinding<VB>(layoutInflater)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB