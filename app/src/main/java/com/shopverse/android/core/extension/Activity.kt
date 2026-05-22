package com.shopverse.android.core.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View

@SuppressLint("SourceLockedOrientationActivity")
fun Activity.lockPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    val root = findViewById<View>(android.R.id.content)
    root.viewTreeObserver.addOnGlobalLayoutListener {
        val cfg = resources.configuration
        if (cfg.orientation != Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}