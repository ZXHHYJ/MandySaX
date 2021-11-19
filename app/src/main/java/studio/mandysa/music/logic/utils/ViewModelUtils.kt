package studio.mandysa.music.logic.utils

import mandysax.fragment.Fragment
import mandysax.fragment.FragmentActivity
import mandysax.lifecycle.Lifecycle
import mandysax.lifecycle.ViewModel
import mandysax.lifecycle.ViewModelProviders
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object ViewModelUtils {

    inline fun <reified VB : ViewModel> FragmentActivity.viewModels() = lazy {
        return@lazy ViewModelProviders.of(
            this
        )[VB::class.java]
    }

    class FragmentActivityViewModelDelegate<VM : ViewModel>(private val clazz: Class<VM>) :
        ReadOnlyProperty<Fragment, VM> {

        private var mViewModel: VM? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): VM {
            if (mViewModel == null) {
                mViewModel =
                    ViewModelProviders.of(
                        thisRef.activity
                    )[clazz]
                thisRef.lifecycle.addObserver {
                    if (it == Lifecycle.Event.ON_DESTROY)
                        mViewModel = null
                }
                thisRef.viewLifecycleOwner.lifecycle.addObserver {
                    if (it == Lifecycle.Event.ON_DESTROY)
                        mViewModel = null
                }
            }
            return mViewModel!!
        }

    }

    inline fun <reified VB : ViewModel> Fragment.viewModels() = lazy {
        return@lazy ViewModelProviders.of(
            viewLifecycleOwner
        )[VB::class.java]
    }

    inline fun <reified VB : ViewModel> activityViewModels() =
        FragmentActivityViewModelDelegate(VB::class.java)
}