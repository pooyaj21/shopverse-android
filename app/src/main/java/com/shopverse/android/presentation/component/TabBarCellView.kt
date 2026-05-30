package com.shopverse.android.presentation.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppVerticalLinearLayout


@SuppressLint("ViewConstructor")
class TabBarCellView(
    context: Context,
    iconRes: Int,
    defaultSelected: Boolean = false
) : AppVerticalLinearLayout(context) {
    private val icon = ImageView(context).apply {
        setImageResource(iconRes)
        imageTintList = ColorStateList.valueOf(
            if (defaultSelected) AppColorProvider.white.value(context)
            else AppColorProvider.gray.value(context)
        )
    }

    private val badge = AppTextView(context).apply {
        isVisible = false
        gravity = Gravity.CENTER
        setTypography(Typography.B10)
        setTextColor(AppColorProvider.alwaysWhite)
        minWidth = BADGE_SIZE_DP.dp
        minHeight = BADGE_SIZE_DP.dp
        setPadding(4.dp, 0, 4.dp, 0)
        background = DrawableBuilder(context)
            .rounded()
            .solidColor(AppColorProvider.discountBadge)
            .build()
    }

    init {
        setPaddingRelative(0, 10.dp, 0, 10.dp)
        val linInner = FrameLayout(context).apply {
            clipChildren = false
            clipToPadding = false
            addView(icon, AppLayout.Frame.get(24.dp).gravity(Gravity.CENTER))
            addView(
                badge,
                AppLayout.Frame.wrapContent()
                    .gravity(Gravity.TOP or Gravity.CENTER)
                    .margin(end = (-12).dp, top = (-8).dp),
            )
        }
        clipChildren = false
        clipToPadding = false
        addView(linInner, AppLayout.Linear.defaultParams())
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        icon.imageTintList = ColorStateList.valueOf(
            if (selected) AppColorProvider.primaryMain.value(context)
            else AppColorProvider.gray.value(context)
        )
    }

    fun setBadgeCount(count: Int) {
        badge.isVisible = count > 0
        badge.text = if (count > MAX_DISPLAY_COUNT) "$MAX_DISPLAY_COUNT+" else count.toString()
    }

    companion object {
        private const val BADGE_SIZE_DP = 16
        private const val MAX_DISPLAY_COUNT = 99
    }
}
