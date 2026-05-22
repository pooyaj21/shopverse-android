package com.shopverse.android

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.typography.Typography

@SuppressLint("ViewConstructor")
class MainView(
    activity: AppCompatActivity,
) : FrameLayout(activity) {
    private val titleTextView = TextView(context).apply {
        text = "ShopVerse"
        setTypography(Typography.B20)
        setTextColor(AppColorProvider.black)
    }

    init {
        addView(
            titleTextView,
            AppLayout.Frame.wrapContent().gravity(Gravity.CENTER)
        )
    }
}