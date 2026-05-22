package com.shopverse.android

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

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
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER)
        )
    }
}