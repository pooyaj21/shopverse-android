package com.shopverse.android.presentation.screen.profile.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.shopverse.android.R
import com.shopverse.android.core.animation.AnimationType
import com.shopverse.android.core.animation.addAnimate
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.applySingleLine
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.extension.setPadding
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppHorizontalLinearLayout
import com.shopverse.android.presentation.component.AppTextView
import com.shopverse.android.presentation.screen.profile.core.ProfileUiModel

@SuppressLint("ViewConstructor")
abstract class ProfileItemView(context: Context) : AppHorizontalLinearLayout(context) {

    protected val titleTextView = AppTextView(context).apply {
        setTypography(Typography.R16)
        setTextColor(AppColorProvider.black)
        applySingleLine()
    }

    init {
        minimumHeight = ROW_HEIGHT_DP.dp
        gravity = Gravity.CENTER_VERTICAL
        setPadding(horizontal = SIDE_PADDING_DP.dp)
        addAnimate(AnimationType.Scale(0.98F))
        addView(
            titleTextView,
            AppLayout.Linear.availableWidthParams().margin(end = SIDE_PADDING_DP.dp)
        )
    }

    protected fun addEndView(view: View, layoutParams: AppLayout.Linear) {
        addView(view, layoutParams)
    }

    class Title(context: Context) : ProfileItemView(context) {
        init {
            minimumHeight = TITLE_HEIGHT_DP.dp
            setBackgroundColor(AppColorProvider.imagePlaceholder)
            titleTextView.apply {
                setTypography(Typography.M14)
                setTextColor(AppColorProvider.gray)
            }
        }

        fun bind(item: ProfileUiModel.Item.Title) {
            titleTextView.text = item.title
        }
    }

    class Navigatable(context: Context) : ProfileItemView(context) {

        private val endIconView = AppCompatImageView(context).apply {
            setImageResource(R.drawable.ic_chevron_end)
            imageTintList = ColorStateList.valueOf(AppColorProvider.gray.value(context))
        }

        init {
            addEndView(endIconView, AppLayout.Linear.get(20.dp))
        }

        fun bind(item: ProfileUiModel.Item.Navigatable) {
            titleTextView.text = item.title
        }
    }

    class Simple(context: Context) : ProfileItemView(context) {
        fun bind(item: ProfileUiModel.Item.Simple) {
            titleTextView.text = item.title
            titleTextView.setTextColor(
                if (item is ProfileUiModel.Item.Simple.Logout) AppColorProvider.discountBadge
                else AppColorProvider.primaryMain
            )
        }
    }

    class Info(context: Context) : ProfileItemView(context) {

        private val valueTextView = AppTextView(context).apply {
            setTypography(Typography.R14)
            setTextColor(AppColorProvider.gray)
            applySingleLine()
        }

        init {
            isClickable = false
            isFocusable = false
            addEndView(valueTextView, AppLayout.Linear.wrapContent())
        }

        fun bind(item: ProfileUiModel.Item.Info) {
            titleTextView.text = item.title
            valueTextView.text = item.value
        }
    }

    class Editable(context: Context) : ProfileItemView(context) {
        private val valueTextView = AppTextView(context).apply {
            setTypography(Typography.R14)
            setTextColor(AppColorProvider.gray)
            applySingleLine()
        }

        private val endIconView = AppCompatImageView(context).apply {
            setImageResource(R.drawable.ic_chevron_end)
            imageTintList = ColorStateList.valueOf(AppColorProvider.gray.value(context))
        }

        init {
            addEndView(
                valueTextView,
                AppLayout.Linear.wrapContent().apply { margin(end = VALUE_END_MARGIN_DP.dp) },
            )
            addEndView(endIconView, AppLayout.Linear.get(20.dp))
        }

        fun bind(item: ProfileUiModel.Item.Editable) {
            titleTextView.text = item.title
            valueTextView.text = item.value
        }
    }

    companion object {
        private const val ROW_HEIGHT_DP = 56
        private const val TITLE_HEIGHT_DP = 36
        private const val SIDE_PADDING_DP = 16
        private const val VALUE_END_MARGIN_DP = 8
    }
}
