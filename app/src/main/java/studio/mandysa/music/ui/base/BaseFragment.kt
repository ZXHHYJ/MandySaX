package studio.mandysa.music.ui.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import mandysax.fragment.Fragment
import mandysax.lifecycle.Lifecycle
import mandysax.lifecycle.LifecycleObserver
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class BaseFragment : Fragment() {

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
                thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                    override fun Observer(state: Lifecycle.Event?) {
                        if (state == Lifecycle.Event.ON_DESTROY)
                            _binding = null
                    }
                })
            }
            return _binding!!
        }
    }

}