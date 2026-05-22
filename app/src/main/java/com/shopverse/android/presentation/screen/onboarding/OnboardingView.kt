package com.shopverse.android.presentation.screen.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.shopverse.android.R
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.component.AppButtonView
import com.shopverse.android.presentation.component.AppTextView

@SuppressLint("ViewConstructor")
class OnboardingView(
    context: Context,
    private val onContinue: () -> Unit,
) : FrameLayout(context) {

    private val iconView = ImageView(context).apply {
        setImageResource(R.mipmap.ic_launcher)
    }

    private val titleView = AppTextView(context).apply {
        text = "ShopVerse"
        setTypography(Typography.B32)
        setTextColor(AppColorProvider.white)
        gravity = Gravity.CENTER
    }

    private val descriptionView = AppTextView(context).apply {
        text = "Your portal to infinite gaming worlds. Browse, buy, and play."
        setTypography(Typography.R16)
        setTextColor(AppColorProvider.white)
        gravity = Gravity.CENTER
    }

    private val continueButton = AppButtonView.OutLine(context).apply {
        text = "Let's Go"
        setOnClickListener { onContinue() }
    }

    private val contentStack = AppVerticalLinearLayout(context).apply {
        gravity = Gravity.CENTER_HORIZONTAL
        addView(iconView, AppLayout.Linear.get(96.dp))
        addView(
            titleView,
            AppLayout.Linear.defaultParams().margin(top = 16.dp)
        )
        addView(
            descriptionView,
            AppLayout.Linear.defaultParams().margin(start = 32.dp, top = 12.dp, end = 32.dp)
        )
    }

    init {
        setBackgroundColor(AppColorProvider.primaryMain)
        addView(
            contentStack,
            AppLayout.Frame.defaultParams().gravity(Gravity.CENTER)
        )
        addView(
            continueButton,
            AppLayout.Frame.defaultParams()
                .gravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
                .margin(start = 32.dp, end = 32.dp, bottom = 48.dp)
        )
    }
}
