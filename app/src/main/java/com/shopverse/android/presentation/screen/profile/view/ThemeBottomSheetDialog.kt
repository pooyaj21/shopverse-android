package com.shopverse.android.presentation.screen.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import androidx.appcompat.widget.AppCompatImageView
import com.shopverse.android.R
import com.shopverse.android.core.animation.AnimationType
import com.shopverse.android.core.animation.addAnimate
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.applySingleLine
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.label
import com.shopverse.android.core.extension.setPadding
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppHorizontalLinearLayout
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.architecture.BaseBottomSheetDialog
import com.shopverse.android.presentation.component.AppTextView
import com.shopverse.core.model.ThemeMode

class ThemeBottomSheetDialog(
    context: Context,
    current: ThemeMode,
    private val onThemeSelectedListener: (ThemeMode) -> Unit,
) : BaseBottomSheetDialog(context) {

    override val title: String = "Theme"

    init {
        val listLayout = AppVerticalLinearLayout(context).apply {
            setPadding(bottom = BOTTOM_PADDING_DP.dp)
        }
        listOf(ThemeMode.LIGHT, ThemeMode.DARK, ThemeMode.SYSTEM).forEach { mode ->
            listLayout.addView(
                OptionView(context, mode, isSelected = mode == current).apply {
                    setOnClickListener {
                        dismiss()
                        onThemeSelectedListener(mode)
                    }
                },
                AppLayout.Linear.defaultParams(),
            )
        }
        setupView(listLayout, AppLayout.WRAP)
    }

    @SuppressLint("ViewConstructor")
    private class OptionView(
        context: Context,
        mode: ThemeMode,
        isSelected: Boolean,
    ) : AppHorizontalLinearLayout(context) {

        init {
            minimumHeight = ROW_HEIGHT_DP.dp
            gravity = Gravity.CENTER_VERTICAL
            setPadding(horizontal = SIDE_PADDING_DP.dp)
            addAnimate(AnimationType.Scale(0.98F))
            addView(
                AppTextView(context).apply {
                    setTypography(if (isSelected) Typography.M16 else Typography.R16)
                    setTextColor(if (isSelected) AppColorProvider.primaryMain else AppColorProvider.black)
                    applySingleLine()
                    text = mode.label
                },
                AppLayout.Linear.availableWidthParams().apply { margin(end = SIDE_PADDING_DP.dp) },
            )
            if (isSelected) {
                addView(
                    AppCompatImageView(context).apply {
                        setImageResource(R.drawable.ic_check)
                        imageTintList = ColorStateList.valueOf(
                            AppColorProvider.primaryMain.value(context)
                        )
                    },
                    AppLayout.Linear.get(20.dp),
                )
            }
        }
    }

    companion object {
        private const val ROW_HEIGHT_DP = 56
        private const val SIDE_PADDING_DP = 16
        private const val BOTTOM_PADDING_DP = 8
    }
}
