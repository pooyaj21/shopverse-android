package com.shopverse.android.presentation.component

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

/**
 * A utility class to switch fragments inside a container view
 *
 * @property containerView The container view to place the fragments inside it
 * @property fragmentManager The child fragment manager of the parent view
 */
class FragmentSwitcher(
    private val containerView: FrameLayout,
    private val fragmentManager: FragmentManager
) {
    private var itemFragmentList: MutableList<ItemFragment<Fragment>> = mutableListOf()
    private var currentFragment: Fragment? = null

    init {
        if (containerView.id == View.NO_ID) {
            throw IllegalArgumentException("Container view's Id is not assigned")
        }
    }

    //****************************************
    //              Public Functions         *
    //****************************************/
    /**
     * This method will switch and replace the input fragment inside the container view
     *
     * @param fragmentTag The fragment's tag that want to switch to it
     */
    fun switch(fragmentTag: String) {
        val item = itemFragmentList.find { itemFragment ->
            itemFragment.tag == fragmentTag
        }
        item?.run {
            currentFragment = fragmentClass.java.getDeclaredConstructor().newInstance().also { fragment ->
                fragmentManager
                    .beginTransaction()
                    .replace(containerView.id, fragment)
                    .commit()
            }
        }
    }

    /**
     * Remove current fragment from the fragment manager while destroying the container view
     */
    fun removeCurrentFragment() {
        currentFragment?.let { fragment ->
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
        currentFragment = null
    }

    /**
     * Add fragment items by this method
     *
     * @param tag The fragment tag
     * @param fragmentClass The fragment kotlin class in order to instantiate it while switching to it
     */
    fun addItem(tag: String, fragmentClass: KClass<Fragment>) = apply {
        itemFragmentList.add(ItemFragment(tag, fragmentClass))
    }

    //****************************************
    //              Helper Classes           *
    //****************************************/
    /**
     * A container class to maintain the fragment switcher items
     */
    private data class ItemFragment<T : Fragment>(val tag: String, val fragmentClass: KClass<T>)
}
