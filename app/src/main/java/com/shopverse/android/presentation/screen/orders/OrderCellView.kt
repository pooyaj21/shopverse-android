package com.shopverse.android.presentation.screen.orders

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.Gravity
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
import com.shopverse.android.core.extension.applySingleLine
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppHorizontalLinearLayout
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.component.AppTextView
import com.shopverse.core.model.OrderSummary
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("ViewConstructor")
class OrderCellView(
    context: Context,
    private val onOrderClick: (String) -> Unit
) : AppHorizontalLinearLayout(context) {

    private val orderIdView = AppTextView(context).apply {
        setTypography(Typography.SB14)
        setTextColor(AppColorProvider.black)
        applySingleLine()
    }

    private val dateView = AppTextView(context).apply {
        setTypography(Typography.R12)
        setTextColor(AppColorProvider.gray)
        applySingleLine()
    }

    private val infoColumn = AppVerticalLinearLayout(context).apply {
        gravity = Gravity.CENTER_VERTICAL
        addView(orderIdView, AppLayout.Linear.defaultParams())
        addView(dateView, AppLayout.Linear.defaultParams().margin(top = 4.dp))
    }

    private val totalView = AppTextView(context).apply {
        setTypography(Typography.SB16)
        setTextColor(AppColorProvider.black)
        applySingleLine()
    }

    private val originalTotalView = AppTextView(context).apply {
        setTypography(Typography.R12)
        setTextColor(AppColorProvider.priceMuted)
        applySingleLine()
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private val totalColumn = AppVerticalLinearLayout(context).apply {
        gravity = Gravity.CENTER_VERTICAL or Gravity.END
        addView(totalView, AppLayout.Linear.wrapContent())
        addView(originalTotalView, AppLayout.Linear.wrapContent().margin(top = 2.dp))
    }

    init {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.cardSurface)
            .cornerRadius(CARD_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        gravity = Gravity.CENTER_VERTICAL
        setPadding(14.dp)
        addView(
            infoColumn,
            AppLayout.Linear.get(0, AppLayout.WRAP).apply {
                weight = 1F
            }
        )
        addView(totalColumn, AppLayout.Linear.wrapContent().margin(start = 12.dp))
    }

    @SuppressLint("SetTextI18n")
    fun bind(order: OrderSummary) {
        orderIdView.text = "Order #${order.id.take(ORDER_ID_VISIBLE_CHARS).uppercase()}"
        dateView.text = formatDate(order.placedAt)
        totalView.text = formatPrice(order.total, order.currency)

        val originalTotal = order.originalTotal
        val hasSavings = originalTotal != null && originalTotal > order.total
        originalTotalView.isVisible = hasSavings
        if (hasSavings) {
            originalTotalView.text = formatPrice(originalTotal, order.currency)
        }
        setOnClickListener { onOrderClick(order.id) }
    }

    private fun formatDate(placedAt: String): String = runCatching {
        OffsetDateTime.parse(placedAt).format(
            DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())
        )
    }.getOrDefault(placedAt.substringBefore('T'))

    @SuppressLint("DefaultLocale")
    private fun formatPrice(value: Double, currency: String): String {
        val rendered = if (value % 1.0 == 0.0) value.toInt().toString()
        else String.format("%.2f", value)
        return "$rendered $currency"
    }

    companion object {
        private const val CARD_CORNER_RADIUS_DP = 18
        private const val ORDER_ID_VISIBLE_CHARS = 8
    }
}
