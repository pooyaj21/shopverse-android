package com.shopverse.android.core

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Insets
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import com.shopverse.android.core.extension.TAG
import kotlin.math.abs
import kotlin.math.ceil

object Screen {
    var size = Size(0,0)
        private set

    fun updateSize(activity: Activity, newConfiguration: Configuration?) {
        val point = activity.getScreenSize(newConfiguration)
        size = Size(point.x, point.y)
        Log.d(TAG, "Screen Size updated: $size")
    }

    data class Size(val width: Int, val height: Int)
}



private fun Activity.getScreenSize(newConfiguration: Configuration?): Point {
    val configuration: Configuration = newConfiguration ?: resources.configuration
    val density = resources.displayMetrics.density
    val screenSize = Point()
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = windowManager.currentWindowMetrics
        // Gets all insets
        val windowInsets = metrics.windowInsets
        val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars()
                    or WindowInsets.Type.displayCutout()
        )
        val insetsWidth: Int = insets.right + insets.left
        val insetsHeight: Int = insets.top + insets.bottom
        // Legacy size that Display#getSize reports
        screenSize.x = metrics.bounds.width() - insetsWidth
        screenSize.y = metrics.bounds.height() - insetsHeight
    } else {
        windowManager.defaultDisplay.getSize(screenSize)
    }
    if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
        val newSize = ceil(configuration.screenWidthDp * density).toInt()
        if (abs(screenSize.x - newSize) > 3) {
            screenSize.x = newSize
        }
    }
    if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
        val newSize = ceil((configuration.screenHeightDp * density)).toInt()
        if (abs(screenSize.y - newSize) > 3) {
            screenSize.y = newSize
        }
    }
    return screenSize
}