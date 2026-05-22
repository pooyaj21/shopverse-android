package com.shopverse.android.presentation.architecture

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.shopverse.android.core.color.AppColor
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.TAG
import com.shopverse.android.core.extension.isDarkModeOn
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.ui.Source


abstract class BaseFragment<V : View> : Fragment() {

    private var statusBar: View? = null
    protected var rootView: V? = null
    private var navBar: View? = null

    protected abstract val currentSource: Source

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        logLifeCycle("onCreateView")
        statusBar = View(requireContext())
        navBar = View(requireContext())
        val contentView = onCreateRootView(inflater, container, savedInstanceState)
        rootView = contentView
        val containerView = AppVerticalLinearLayout(requireContext()).apply {
            addView(statusBar, AppLayout.Linear.get(AppLayout.MATCH, 0))
            addView(contentView, AppLayout.Linear.justOccupyScreen())
            addView(navBar, AppLayout.Linear.get(AppLayout.MATCH, 0))
        }
        ViewCompat.setOnApplyWindowInsetsListener(contentView) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            statusBar?.updateLayoutParams {
                height = systemBars.top
            }

            navBar?.updateLayoutParams {
                height = systemBars.bottom
            }

            WindowInsetsCompat.CONSUMED
        }
        setNavBarMode()
        setStatusBarIconsDark()
        return containerView
    }


    override fun onResume() {
        super.onResume()
        logLifeCycle("onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifeCycle("onDestroy")
    }

    abstract fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): V

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logLifeCycle("logLifeCycle")
        statusBarColor(AppColorProvider.background)
        setNavBarMode()
        setStatusBarIconsDark()
        // Default Background Color
        view.setBackgroundColor(AppColorProvider.background)
    }

    protected open fun onDestroyRootView() {
        // Implement if needed
    }

    @CallSuper
    final override fun onDestroyView() {
        logLifeCycle("onDestroyView")
        onDestroyRootView()
        rootView = null
        statusBar = null
        navBar = null
        super.onDestroyView()
    }
    //****************************************
    //             Helper Functions          *
    //****************************************/

    /**
     * A convenience function to find all lifeCycles in Logcat by searching "lifeCycle" keyword
     * */
    protected fun logLifeCycle(lifeCycle: String) {
        Log.d(TAG, "$TAG ($this) lifeCycle: $lifeCycle")
    }

    protected fun statusBarColor(color: AppColor) {
        statusBarColor(color.value(requireContext()))
    }

    @Suppress("DEPRECATION")
    protected fun statusBarColor(color: Int) {
        requireActivity().window.statusBarColor = color
        statusBar?.let {
            it.isVisible = true
            it.setBackgroundColor(color)
            it.invalidate()
        }
    }

    protected fun setStatusBarIconsDark(isDarkModeOn: Boolean? = null) {
        val shouldBeDark = isDarkModeOn ?: requireContext().isDarkModeOn.not()
        requireActivity().window?.let { window ->
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = shouldBeDark
        }
    }

    @Suppress("DEPRECATION")
    protected fun setNavBarMode(isDarkModeOn: Boolean? = null) {
        val isDarkModeOn = isDarkModeOn ?: requireContext().isDarkModeOn.not()
        val navColor = AppColorProvider.background
        requireActivity().window.navigationBarColor = navColor.value(requireContext())
        navBar?.let {
            it.isVisible = true
            it.setBackgroundColor(navColor.value(requireContext()))
            it.invalidate()
        }
        requireActivity().window?.let { window ->
            WindowCompat.getInsetsController(
                window,
                window.decorView
            ).isAppearanceLightNavigationBars = isDarkModeOn
        }
    }

    @Suppress("DEPRECATION")
    protected fun removeStatusBar(isDarkModeOn: Boolean? = null) {
        setStatusBarIconsDark(isDarkModeOn)
        requireActivity().window.statusBarColor = Color.TRANSPARENT
        statusBar?.isVisible = false
    }

    @Suppress("DEPRECATION")
    protected fun removeNavBar(isDarkModeOn: Boolean? = null) {
        setNavBarMode(isDarkModeOn)
        requireActivity().window.navigationBarColor = Color.TRANSPARENT
        navBar?.isVisible = false
    }

    protected fun fullScreen() {
        removeStatusBar()
        removeNavBar()
    }
}