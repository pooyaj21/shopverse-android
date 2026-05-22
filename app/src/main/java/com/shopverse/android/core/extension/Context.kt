@file:Suppress("Unused")

package com.shopverse.android.core.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager

val Context.isDarkModeOn: Boolean
    get() {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES ->
                true // Night mode is active, we're using dark theme
            else ->
                false // Night mode is not active, we're using the light theme
        }
    }

val Context.navigationBarHeight: Int
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val insets = windowManager.currentWindowMetrics.windowInsets
            insets.getInsets(WindowInsets.Type.navigationBars()).bottom
        } else {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val height = if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
            height.coerceAtLeast(60)
        }
    }

fun Context.isRTL() = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL


