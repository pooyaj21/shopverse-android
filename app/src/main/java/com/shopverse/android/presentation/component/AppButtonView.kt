package com.shopverse.android.presentation.component

import android.content.Context
import android.view.Gravity
import androidx.core.view.setPadding
import com.shopverse.android.core.animation.AnimationType
import com.shopverse.android.core.animation.addAnimate
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
import com.shopverse.android.core.extension.applySingleLine
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppHorizontalLinearLayout

open class AppButtonView(context: Context) : AppHorizontalLinearLayout(context) {

    private val textView = AppTextView(context).apply {
        setTypography(Typography.M16)
        applySingleLine()
    }

    var text: String
        get() = textView.text.toString()
        set(value) {
            textView.text = value
        }

    init {
        gravity = Gravity.CENTER
        addAnimate(AnimationType.Scale(0.95F))
        setPadding(12.dp)
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.buttonFilled)
            .cornerRadius(12.dp)
            .build()
        addView(textView, AppLayout.Linear.wrapContent())
        isEnabled = true
    }

    override fun setEnabled(enabled: Boolean) {
        textView.setTextColor(if (enabled) AppColorProvider.white else AppColorProvider.gray)
    }

    class OutLine(context: Context) : AppButtonView(context) {
        init {
            background = DrawableBuilder(context)
                .strokeWidth(1.dp)
                .strokeColor(AppColorProvider.buttonOutline)
                .solidColor(AppColorProvider.background)
                .cornerRadius(12.dp)
                .build()
        }
    }
}