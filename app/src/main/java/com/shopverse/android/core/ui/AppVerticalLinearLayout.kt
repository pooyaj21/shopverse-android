package com.shopverse.android.core.ui

import android.content.Context
import android.widget.LinearLayout

open class AppVerticalLinearLayout(context: Context) : LinearLayout(context) {
    init {
        orientation = VERTICAL
    }
}