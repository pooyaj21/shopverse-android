package com.shopverse.android.core.ui

import android.content.Context
import android.widget.LinearLayout

open class AppHorizontalLinearLayout(context: Context) : LinearLayout(context) {

    init {
        orientation = HORIZONTAL
    }
}