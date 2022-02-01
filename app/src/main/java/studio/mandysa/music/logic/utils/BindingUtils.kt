package studio.mandysa.music.logic.utils

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import mandysax.fragment.Fragment
import mandysax.fragment.FragmentActivity
import mandysax.lifecycle.Lifecycle
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VB : ViewBinding> FragmentActivity.inflate() = lazy {
    inflateBinding<VB>(layoutInflater)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB


inline fun <reified VB : ViewBinding> bindView() =
    FragmentBindingDelegate(VB::class.java)

class FragmentBindingDelegate<VB : ViewBinding>(
    private val clazz: Class<VB>
) : ReadOnlyProperty<Fragment, VB> {

    private var mBinding: VB? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (mBinding == null) {
            mBinding = clazz.getMethod("inflate", LayoutInflater::class.java)
                .invoke(null, thisRef.layoutInflater) as VB
            thisRef.viewLifecycleOwner.lifecycle.addObserver {
                if (it == Lifecycle.Event.ON_DESTROY)
                    mBinding = null
            }
        }
        return mBinding!!
    }
}