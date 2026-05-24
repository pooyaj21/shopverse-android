package com.shopverse.android.presentation.screen.cart

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.presentation.component.AppTextView

@SuppressLint("ViewConstructor")
class CartView(context: Context) : FrameLayout(context) {

    private val titleView = AppTextView(context).apply {
        text = "Cart"
        setTypography(Typography.SB20)
        setTextColor(AppColorProvider.black)
    }

    init {
        addView(
            titleView,
            AppLayout.Frame.wrapContent().gravity(Gravity.CENTER)
        )
    }
}
