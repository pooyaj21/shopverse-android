package com.shopverse.android.presentation.screen.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.presentation.component.AppTextView

@SuppressLint("ViewConstructor")
class ProfileView(context: Context) : FrameLayout(context) {

    private val label = AppTextView(context).apply {
        text = "Profile"
        textSize = 20f
        setTextColor(AppColorProvider.black.value(context))
    }

    init {
        addView(label, AppLayout.Frame.wrapContent().gravity(Gravity.CENTER))
    }
}
