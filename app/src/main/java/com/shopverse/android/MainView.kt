package com.shopverse.android

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.shopverse.android.core.layout.AppLayout

@SuppressLint("ViewConstructor")
class MainView(
    activity: AppCompatActivity,
) : FrameLayout(activity) {
    private val titleTextView = TextView(context).apply {
        text = "ShopVerse"
    }

    init {
        addView(
            titleTextView,
            AppLayout.Frame.wrapContent().gravity(Gravity.CENTER)
        )
    }
}