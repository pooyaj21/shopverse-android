package com.shopverse.android.presentation.screen.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.typography.Typography

@SuppressLint("ViewConstructor")
class HomeView(context: Context) : FrameLayout(context) {

    private val titleView = TextView(context).apply {
        text = "Home"
        setTypography(Typography.B24)
        setTextColor(AppColorProvider.primaryMain)
    }

    init {
        addView(
            titleView,
            AppLayout.Frame.wrapContent().gravity(Gravity.CENTER)
        )
    }
}
