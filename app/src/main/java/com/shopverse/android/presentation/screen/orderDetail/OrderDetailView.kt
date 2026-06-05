package com.shopverse.android.presentation.screen.orderDetail

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
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
import com.shopverse.android.core.qr.QrCodeGenerator
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppHorizontalLinearLayout
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.architecture.BaseView
import com.shopverse.android.presentation.component.AppTextView
import com.shopverse.core.model.OrderLineItem
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("ViewConstructor")
class OrderDetailView(
    context: Context,
    onRetryClickListener: () -> Unit,
    private val onItemClickListener: (productId: String) -> Unit,
) : BaseView.State<OrderDetailUiModel>(context, onRetryClickListener) {

    override val title: String = "Order Details"

    override fun remoteErrorMessage(httpCode: Int, message: String?): String =
        "Couldn't load this order ($httpCode). Tap to retry."

    // Header --------------------------------------------------------------

    private val orderIdRow = InfoRow(context, label = "Order")
    private val placedAtRow = InfoRow(context, label = "Placed")

    private val headerCard = buildCard().apply {
        addView(orderIdRow, AppLayout.Linear.defaultParams())
        addView(buildDivider(), dividerParams())
        addView(placedAtRow, AppLayout.Linear.defaultParams())
    }

    // Items ---------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    private val itemsTitleView = buildSectionTitle().apply { text = "Items" }

    private val itemsCard = buildCard()

    // Summary -------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    private val summaryTitleView = buildSectionTitle().apply { text = "Summary" }

    private val originalTotalRow = InfoRow(context, label = "Original total").apply {
        strikeThroughValue()
    }
    private val savingsRow = InfoRow(context, label = "You saved").apply {
        setValueColor(AppColorProvider.discountBadge)
    }
    private val totalRow = InfoRow(
        context,
        label = "Total",
        labelTypography = Typography.SB16,
        valueTypography = Typography.SB16,
    ).apply {
        setValueColor(AppColorProvider.black)
    }
    private val originalTotalDivider = buildDivider()
    private val savingsDivider = buildDivider()

    private val summaryCard = buildCard().apply {
        addView(originalTotalRow, AppLayout.Linear.defaultParams())
        addView(originalTotalDivider, dividerParams())
        addView(savingsRow, AppLayout.Linear.defaultParams())
        addView(savingsDivider, dividerParams())
        addView(totalRow, AppLayout.Linear.defaultParams())
    }

    // Receipt QR ----------------------------------------------------------

    @SuppressLint("SetTextI18n")
    private val receiptTitleView = buildSectionTitle().apply { text = "Receipt" }

    private val qrImageView = ImageView(context).apply {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.alwaysWhite)
            .cornerRadius(QR_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
    }

    private val receiptCard = buildCard().apply {
        gravity = Gravity.CENTER_HORIZONTAL
        setPadding(vertical = SIDE_PADDING_DP.dp)
        addView(qrImageView, AppLayout.Linear.get(QR_SIZE_DP.dp))
    }

    // Content -------------------------------------------------------------

    private val contentColumn = AppVerticalLinearLayout(context).apply {
        setPadding(horizontal = EDGE_DP.dp)
        addView(headerCard, AppLayout.Linear.defaultParams().margin(top = EDGE_DP.dp))
        addView(itemsTitleView, AppLayout.Linear.defaultParams().margin(top = 16.dp))
        addView(itemsCard, AppLayout.Linear.defaultParams().margin(top = 8.dp))
        addView(summaryTitleView, AppLayout.Linear.defaultParams().margin(top = 16.dp))
        addView(summaryCard, AppLayout.Linear.defaultParams().margin(top = 8.dp))
        addView(receiptTitleView, AppLayout.Linear.defaultParams().margin(top = 16.dp))
        addView(
            receiptCard,
            AppLayout.Linear.defaultParams().margin(top = 8.dp, bottom = EDGE_DP.dp)
        )
    }

    private val scrollView = ScrollView(context).apply {
        isVerticalScrollBarEnabled = false
        addView(contentColumn, AppLayout.Scroll.defaultParams())
    }

    init {
        setContent(scrollView)
    }

    @SuppressLint("SetTextI18n")
    override fun renderSuccess(model: OrderDetailUiModel) {
        val order = model.order

        orderIdRow.value = "#${order.id.take(ORDER_ID_VISIBLE_CHARS).uppercase()}"
        placedAtRow.value = formatDate(order.placedAt)

        bindItems(model)
        bindSummary(model)
        bindReceipt(model)
    }

    private fun bindReceipt(model: OrderDetailUiModel) {
        if (model.deeplink != renderedDeeplink) {
            renderedDeeplink = model.deeplink
            qrImageView.setImageBitmap(
                QrCodeGenerator.generate(content = model.deeplink, sizePx = QR_SIZE_DP.dp)
            )
        }
    }

    private var renderedDeeplink: String? = null

    private fun bindItems(model: OrderDetailUiModel) {
        itemsCard.removeAllViews()
        model.order.items.forEachIndexed { index, item ->
            if (index > 0) itemsCard.addView(buildDivider(), dividerParams())
            itemsCard.addView(
                LineItemRow(context, item, model.order.currency, onItemClickListener),
                AppLayout.Linear.defaultParams(),
            )
        }
        itemsCard.isVisible = model.order.items.isNotEmpty()
        itemsTitleView.isVisible = model.order.items.isNotEmpty()
    }

    private fun bindSummary(model: OrderDetailUiModel) {
        val order = model.order
        val savings = model.savings
        val hasSavings = savings != null

        originalTotalRow.isVisible = hasSavings
        originalTotalDivider.isVisible = hasSavings
        savingsRow.isVisible = hasSavings
        savingsDivider.isVisible = hasSavings
        if (savings != null) {
            originalTotalRow.value = formatPrice(order.originalTotal!!, order.currency)
            savingsRow.value = formatPrice(savings, order.currency)
        }
        totalRow.value = formatPrice(order.total, order.currency)
    }

    // Builders ------------------------------------------------------------

    private fun buildCard(): AppVerticalLinearLayout =
        AppVerticalLinearLayout(context).apply {
            background = DrawableBuilder(context)
                .solidColor(AppColorProvider.cardSurface)
                .cornerRadius(CARD_CORNER_RADIUS_DP.dp)
                .build()
            clipToOutline = true
        }

    private fun buildSectionTitle(): AppTextView =
        AppTextView(context).apply {
            setTypography(Typography.SB16)
            setTextColor(AppColorProvider.black)
        }

    private fun buildDivider(): View =
        View(context).apply { setBackgroundColor(AppColorProvider.imagePlaceholder) }

    private fun dividerParams() =
        AppLayout.Linear.horizontalLine().margin(start = SIDE_PADDING_DP.dp)

    private fun formatDate(placedAt: String): String = runCatching {
        OffsetDateTime.parse(placedAt).format(
            DateTimeFormatter.ofPattern("MMM d, yyyy · HH:mm", Locale.getDefault())
        )
    }.getOrDefault(placedAt.substringBefore('T'))

    // Rows ----------------------------------------------------------------

    private class InfoRow(
        context: Context,
        label: String,
        labelTypography: Typography = Typography.R14,
        valueTypography: Typography = Typography.M14,
    ) : AppHorizontalLinearLayout(context) {

        private val valueTextView = AppTextView(context).apply {
            setTypography(valueTypography)
            setTextColor(AppColorProvider.black)
            applySingleLine()
        }

        var value: String
            get() = valueTextView.text.toString()
            set(newValue) {
                valueTextView.text = newValue
            }

        fun setValueColor(color: com.shopverse.android.core.color.AppColor) {
            valueTextView.setTextColor(color)
        }

        fun strikeThroughValue() {
            valueTextView.setTextColor(AppColorProvider.priceMuted)
            valueTextView.paintFlags = valueTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        init {
            minimumHeight = ROW_HEIGHT_DP.dp
            gravity = Gravity.CENTER_VERTICAL
            setPadding(horizontal = SIDE_PADDING_DP.dp)
            addView(
                AppTextView(context).apply {
                    setTypography(labelTypography)
                    setTextColor(AppColorProvider.gray)
                    applySingleLine()
                    text = label
                },
                AppLayout.Linear.availableWidthParams().margin(end = SIDE_PADDING_DP.dp)
            )
            addView(valueTextView, AppLayout.Linear.wrapContent())
        }
    }

    @SuppressLint("SetTextI18n")
    private class LineItemRow(
        context: Context,
        item: OrderLineItem,
        currency: String,
        onItemClickListener: (productId: String) -> Unit,
    ) : AppHorizontalLinearLayout(context) {

        init {
            gravity = Gravity.CENTER_VERTICAL
            setPadding(horizontal = SIDE_PADDING_DP.dp, vertical = 12.dp)

            val imageView = ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                background = DrawableBuilder(context)
                    .solidColor(AppColorProvider.imagePlaceholder)
                    .cornerRadius(ITEM_IMAGE_CORNER_RADIUS_DP.dp)
                    .build()
                clipToOutline = true
                // Deleted products have no catalog image; the placeholder
                // background stands in.
                if (item.productImage != null) {
                    load(item.productImage) {
                        crossfade(true)
                        transformations(
                            RoundedCornersTransformation(ITEM_IMAGE_CORNER_RADIUS_DP.dp.toFloat())
                        )
                    }
                }
            }
            addView(imageView, AppLayout.Linear.get(ITEM_IMAGE_SIZE_DP.dp))

            val infoColumn = AppVerticalLinearLayout(context).apply {
                addView(
                    AppTextView(context).apply {
                        setTypography(Typography.SB14)
                        setTextColor(AppColorProvider.black)
                        applySingleLine()
                        text = item.productTitle
                    },
                    AppLayout.Linear.defaultParams(),
                )
                addView(
                    AppTextView(context).apply {
                        setTypography(Typography.R12)
                        setTextColor(AppColorProvider.gray)
                        applySingleLine()
                        text = "${item.quantity} × ${formatPrice(item.unitPrice, currency)}"
                    },
                    AppLayout.Linear.defaultParams().margin(top = 2.dp),
                )
            }
            addView(
                infoColumn,
                AppLayout.Linear.get(0, AppLayout.WRAP)
                    .apply { weight = 1F }
                    .margin(start = 12.dp)
            )
            addView(
                AppTextView(context).apply {
                    setTypography(Typography.SB14)
                    setTextColor(AppColorProvider.black)
                    applySingleLine()
                    text = formatPrice(item.lineTotal, currency)
                },
                AppLayout.Linear.wrapContent().margin(start = 12.dp)
            )

            // Deleted products keep their line-item snapshot but have no
            // catalog page to open.
            val productId = item.productId
            if (productId != null) setOnClickListener { onItemClickListener(productId) }
        }
    }

    companion object {
        private const val CARD_CORNER_RADIUS_DP = 18
        private const val ROW_HEIGHT_DP = 48
        private const val SIDE_PADDING_DP = 16
        private const val EDGE_DP = 12
        private const val ORDER_ID_VISIBLE_CHARS = 8
        private const val ITEM_IMAGE_SIZE_DP = 56
        private const val ITEM_IMAGE_CORNER_RADIUS_DP = 10
        private const val QR_SIZE_DP = 180
        private const val QR_CORNER_RADIUS_DP = 14

        @SuppressLint("DefaultLocale")
        private fun formatPrice(value: Double, currency: String): String {
            val rendered = if (value % 1.0 == 0.0) value.toInt().toString()
            else String.format("%.2f", value)
            return "$rendered $currency"
        }
    }
}
