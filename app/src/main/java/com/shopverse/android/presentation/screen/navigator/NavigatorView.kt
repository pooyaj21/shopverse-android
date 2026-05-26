@file:Suppress("UNCHECKED_CAST", "Unused")

package com.shopverse.android.presentation.screen.navigator

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.shopverse.android.R
import com.shopverse.android.core.extension.TAG
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.presentation.component.FragmentSwitcher
import com.shopverse.android.presentation.component.TabBarCellView
import com.shopverse.android.presentation.component.TabBarView
import com.shopverse.android.presentation.screen.cart.CartFragment
import com.shopverse.android.presentation.screen.home.HomeFragment
import com.shopverse.android.presentation.screen.profile.ProfileFragment
import java.util.Stack
import kotlin.reflect.KClass

@SuppressLint("ViewConstructor")
class NavigatorView(
    fragment: Fragment,
    private val tabBarBackStack: Stack<TabBarView.Tab>?,
    private val onTabSelectedListener: (isDefaultTab: Boolean, currentTabTag: String) -> Unit,
) : FrameLayout(fragment.requireContext()) {

    companion object {
        const val TAB_HOME = "tab_home"
        const val TAB_CART = "tab_cart"
        const val TAB_PROFILE = "tab_profile"
    }

    private val tabs: List<TabContainer> = createTabContainerList()

    private val fragmentContainer = FrameLayout(context).apply {
        id = generateViewId()
    }

    // Must be initialized before tabBarView: TabBarView.build() synchronously
    // selects the default tab, which fires onItemSelected -> fragmentSwitcher.switch().
    private val fragmentSwitcher: FragmentSwitcher =
        FragmentSwitcher(fragmentContainer, fragment.childFragmentManager).apply {
            tabs.forEach { item ->
                addItem(item.tag, item.fragmentClass)
            }
        }

    val tabBarView = TabBarView.Builder(context, TAB_HOME).apply {
        tabs.forEach { addItem(it.tag, it.view) }
        setOnItemSelectedListener(object : TabBarView.OnItemSelectedListener {
            override fun onItemSelected(tab: TabBarView.Tab) {
                Log.d(this@NavigatorView.TAG, "OnItemSelectedListener.onItemSelected: $tab")
                onTabSelectedListener.invoke(tab.tag == TAB_HOME, tab.tag)
                fragmentSwitcher.switch(tab.tag)
            }
        })
        if (tabBarBackStack != null) {
            addBackStack(tabBarBackStack)
        }
    }.build().apply {
        clipToPadding = false
        clipToOutline = false
        elevation = 10.dp.toFloat()
        setPadding(3.dp)
    }

    init {
        clipToPadding = false
        clipChildren = false

        // Setup Fragment Container
        addView(fragmentContainer, AppLayout.Frame.fullScreen())
        addView(
            tabBarView,
            AppLayout.Frame.defaultParams().gravity(Gravity.BOTTOM)
        )
    }

    fun getBackstack(): Stack<TabBarView.Tab> = tabBarView.getBackStack()

    private fun createTabContainerList() = listOf(
        TabContainer(
            tag = TAB_HOME,
            fragmentClass = HomeFragment::class as KClass<Fragment>,
            view = TabBarCellView(
                context = context,
                iconRes = R.drawable.ic_home,
                defaultSelected = true
            )
        ),
        TabContainer(
            tag = TAB_CART,
            fragmentClass = CartFragment::class as KClass<Fragment>,
            view = TabBarCellView(
                context = context,
                iconRes = R.drawable.ic_cart
            )
        ),
        TabContainer(
            tag = TAB_PROFILE,
            fragmentClass = ProfileFragment::class as KClass<Fragment>,
            view = TabBarCellView(
                context = context,
                iconRes = R.drawable.ic_profile
            )
        ),
    )

    fun onDestroyView() {
        fragmentSwitcher.removeCurrentFragment()
    }

    data class TabContainer(
        var tag: String,
        val fragmentClass: KClass<Fragment>,
        val view: TabBarCellView
    )
}