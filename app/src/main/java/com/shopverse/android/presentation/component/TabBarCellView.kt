package com.shopverse.android.presentation.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.layout.AppLayout
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

    init {
        setPaddingRelative(0, 10.dp, 0, 10.dp)
        val linInner = FrameLayout(context).apply {
            addView(icon, AppLayout.Frame.get(24.dp).gravity(Gravity.CENTER))
        }
        addView(linInner, AppLayout.Linear.defaultParams())
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        icon.imageTintList = ColorStateList.valueOf(
            if (selected) AppColorProvider.primaryMain.value(context)
            else AppColorProvider.gray.value(context)
        )
    }
}
