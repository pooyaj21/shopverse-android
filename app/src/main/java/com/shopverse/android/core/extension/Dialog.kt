package com.shopverse.android.core.extension

import android.app.Dialog
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

fun Dialog.setDismissOnHiddenBehavior() {
    setOnShowListener {
        val bottomSheet = findViewById<View>(
            com.google.android.material.R.id.design_bottom_sheet
        ) as? FrameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        // Setup Expanded by default
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }
}