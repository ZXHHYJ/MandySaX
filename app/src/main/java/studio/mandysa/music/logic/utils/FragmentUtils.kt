package studio.mandysa.music.logic.utils

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import mandysax.fragment.Fragment
import mandysax.lifecycle.Lifecycle
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VB : ViewBinding> bindView() =
    FragmentBindingDelegate(VB::class.java)

class FragmentBindingDelegate<VB : ViewBinding>(
    private val clazz: Class<VB>
) : ReadOnlyProperty<Fragment, VB> {

    private var _binding: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (_binding == null) {
            _binding = clazz.getMethod("inflate", LayoutInflater::class.java)
                .invoke(null, thisRef.layoutInflater) as VB
            thisRef.viewLifecycleOwner.lifecycle.addObserver {
                if (it == Lifecycle.Event.ON_DESTROY)
                    _binding = null
            }
        }
        return _binding!!
    }
}