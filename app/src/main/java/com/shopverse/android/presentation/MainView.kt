package com.shopverse.android.presentation

import android.annotation.SuppressLint
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.shopverse.android.R
import com.shopverse.android.core.layout.AppLayout

@SuppressLint("ViewConstructor")
class MainView(
    private val activity: AppCompatActivity,
) : FrameLayout(activity) {

    private val fragmentContainer = FrameLayout(activity).apply {
        id = R.id.root_view_container
    }

    init {
        addView(fragmentContainer, AppLayout.Frame.fullScreen())
    }

    fun setNavigation() {
        if (activity.supportFragmentManager.fragments.isNotEmpty()) return
        val navHost = NavHostFragment.create(graphResId = R.navigation.navigation_main)
        activity.supportFragmentManager.beginTransaction()
            .replace(fragmentContainer.id, navHost)
            .setPrimaryNavigationFragment(navHost)
            .commitAllowingStateLoss()
    }
}
