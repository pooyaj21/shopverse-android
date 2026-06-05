package com.shopverse.android.presentation.screen.account

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
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
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.architecture.BaseView
import com.shopverse.android.presentation.component.AppButtonView
import com.shopverse.android.presentation.component.AppTextView

@SuppressLint("ViewConstructor")
class AccountView(
    context: Context,
    onDeleteAccountClickListener: OnClickListener,
) : BaseView.State<AccountUiModel>(context, onRetryClickListener = {}) {

    override val title: String = "Profile"
    override val emptyMessage: String = "You're not logged in."

    private val nameRow = InfoRow(context, label = "Name")
    private val emailRow = InfoRow(context, label = "Email")

    private val infoCard = AppVerticalLinearLayout(context).apply {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.cardSurface)
            .cornerRadius(CARD_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        addView(nameRow, AppLayout.Linear.defaultParams())
        addView(
            View(context).apply { setBackgroundColor(AppColorProvider.imagePlaceholder) },
            AppLayout.Linear.horizontalLine()
                .margin(
                    start = SIDE_PADDING_DP.dp,
                    bottom = GAP_PADDING_DP.dp,
                    top = GAP_PADDING_DP.dp
                )
        )
        addView(emailRow, AppLayout.Linear.defaultParams())
    }

    private val deleteButton = AppButtonView(context).apply {
        text = "Delete Account"
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.discountBadge)
            .cornerRadius(12.dp)
            .build()
        setOnClickListener { onDeleteAccountClickListener.onClick(it) }
    }

    private val contentContainer = AppVerticalLinearLayout(context).apply {
        addView(
            infoCard,
            AppLayout.Linear.defaultParams().margin(
                start = EDGE_DP.dp,
                end = EDGE_DP.dp,
                top = EDGE_DP.dp,
            )
        )
        addView(View(context), AppLayout.Linear.availableHeightParams())
        addView(
            deleteButton,
            AppLayout.Linear.defaultParams().margin(
                start = EDGE_DP.dp,
                end = EDGE_DP.dp,
                bottom = BOTTOM_MARGIN_DP.dp,
            )
        )
    }

    init {
        setContent(contentContainer)
    }

    override fun renderSuccess(model: AccountUiModel) {
        nameRow.value = model.name ?: "—"
        emailRow.value = model.email ?: "—"
    }

    private class InfoRow(context: Context, label: String) : AppHorizontalLinearLayout(context) {

        private val valueTextView = AppTextView(context).apply {
            setTypography(Typography.R14)
            setTextColor(AppColorProvider.gray)
            applySingleLine()
        }

        var value: String
            get() = valueTextView.text.toString()
            set(newValue) {
                valueTextView.text = newValue
            }

        init {
            minimumHeight = ROW_HEIGHT_DP.dp
            gravity = Gravity.CENTER_VERTICAL
            setPadding(horizontal = SIDE_PADDING_DP.dp)
            addView(
                AppTextView(context).apply {
                    setTypography(Typography.R16)
                    setTextColor(AppColorProvider.black)
                    applySingleLine()
                    text = label
                },
                AppLayout.Linear.availableWidthParams().margin(end = SIDE_PADDING_DP.dp)
            )
            addView(valueTextView, AppLayout.Linear.wrapContent())
        }
    }

    companion object {
        private const val CARD_CORNER_RADIUS_DP = 18
        private const val ROW_HEIGHT_DP = 56
        private const val SIDE_PADDING_DP = 16
        private const val GAP_PADDING_DP = 8
        private const val EDGE_DP = 12
        private const val BOTTOM_MARGIN_DP = 16
    }
}
