package com.shopverse.android.presentation.screen.productDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
import com.shopverse.android.R
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
import com.shopverse.core.model.Product
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class ProductDetailView(
    context: Context,
    onRetryClickListener: () -> Unit,
    private val onAddToCartClickListener: OnClickListener,
    private val onGoToCartClickListener: OnClickListener,
) : BaseView.State<ProductDetailUiModel>(context, onRetryClickListener = onRetryClickListener) {

    override val title: String = ""

    // Cover -------------------------------------------------------------

    private val imageView = ImageView(context).apply {
        scaleType = ImageView.ScaleType.FIT_CENTER
        setBackgroundColor(AppColorProvider.imagePlaceholder)
    }

    private val discountBadge = AppTextView(context).apply {
        setTypography(Typography.SB12)
        setTextColor(AppColorProvider.alwaysWhite)
        setPadding(horizontal = 8.dp, vertical = 2.dp)
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.discountBadge)
            .cornerRadius(999.dp)
            .build()
        isVisible = false
    }

    private val imageContainer = FrameLayout(context).apply {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.imagePlaceholder)
            .cornerRadius(IMAGE_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        addView(imageView, AppLayout.Frame.get(AppLayout.MATCH, IMAGE_HEIGHT_DP.dp))
        addView(
            discountBadge,
            AppLayout.Frame.wrapContent()
                .gravity(Gravity.TOP or Gravity.START)
                .margin(start = 8.dp, top = 8.dp)
        )
    }

    // Header ------------------------------------------------------------

    private val titleView = AppTextView(context).apply {
        setTypography(Typography.B20)
        setTextColor(AppColorProvider.black)
    }

    private val makerView = AppTextView(context).apply {
        setTypography(Typography.R14)
        setTextColor(AppColorProvider.gray)
        applySingleLine()
    }

    private val ratingIcon = ImageView(context).apply {
        setImageResource(R.drawable.ic_star)
        imageTintList = ColorStateList.valueOf(AppColorProvider.ratingStar.value(context))
    }

    private val ratingText = AppTextView(context).apply {
        setTypography(Typography.M12)
        setTextColor(AppColorProvider.black)
        applySingleLine()
    }

    private val ratingCountText = AppTextView(context).apply {
        setTypography(Typography.R12)
        setTextColor(AppColorProvider.priceMuted)
        applySingleLine()
    }

    private val ratingRow = AppHorizontalLinearLayout(context).apply {
        gravity = Gravity.CENTER_VERTICAL
        addView(ratingIcon, AppLayout.Linear.get(14.dp))
        addView(ratingText, AppLayout.Linear.wrapContent().margin(start = 4.dp))
        addView(ratingCountText, AppLayout.Linear.wrapContent().margin(start = 4.dp))
    }

    // Tags --------------------------------------------------------------

    private val tagsRow = AppHorizontalLinearLayout(context)

    private val tagsScroll = HorizontalScrollView(context).apply {
        isHorizontalScrollBarEnabled = false
        addView(tagsRow, AppLayout.Frame.wrapContent())
    }

    // Facts -------------------------------------------------------------

    private val releaseDateRow = FactRow(context, label = "Release date")
    private val stockRow = FactRow(context, label = "Availability")

    private val factsCard = AppVerticalLinearLayout(context).apply {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.cardSurface)
            .cornerRadius(CARD_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        addView(releaseDateRow, AppLayout.Linear.defaultParams())
        addView(
            android.view.View(context).apply {
                setBackgroundColor(AppColorProvider.imagePlaceholder)
            },
            AppLayout.Linear.horizontalLine().margin(start = SIDE_PADDING_DP.dp)
        )
        addView(stockRow, AppLayout.Linear.defaultParams())
    }

    // About -------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    private val aboutTitleView = AppTextView(context).apply {
        setTypography(Typography.SB16)
        setTextColor(AppColorProvider.black)
        text = "About"
    }

    private val descriptionView = AppTextView(context).apply {
        setTypography(Typography.R14)
        setTextColor(AppColorProvider.gray)
        setLineSpacing(4.dp.toFloat(), 1F)
    }

    // Scroll content ------------------------------------------------------

    private val scrollContent = AppVerticalLinearLayout(context).apply {
        setPadding(horizontal = EDGE_DP.dp)
        addView(imageContainer, AppLayout.Linear.defaultParams())
        addView(titleView, AppLayout.Linear.defaultParams().margin(top = 14.dp))
        addView(makerView, AppLayout.Linear.defaultParams().margin(top = 4.dp))
        addView(ratingRow, AppLayout.Linear.defaultParams().margin(top = 8.dp))
        addView(tagsScroll, AppLayout.Linear.defaultParams().margin(top = 12.dp))
        addView(factsCard, AppLayout.Linear.defaultParams().margin(top = 12.dp))
        addView(aboutTitleView, AppLayout.Linear.defaultParams().margin(top = 16.dp))
        addView(descriptionView, AppLayout.Linear.defaultParams().margin(top = 6.dp, bottom = 16.dp))
    }

    private val scrollView = ScrollView(context).apply {
        isVerticalScrollBarEnabled = false
        addView(scrollContent, AppLayout.Scroll.defaultParams())
    }

    // Bottom bar ----------------------------------------------------------

    private val priceView = AppTextView(context).apply {
        setTypography(Typography.SB20)
        setTextColor(AppColorProvider.black)
        applySingleLine()
    }

    private val oldPriceView = AppTextView(context).apply {
        setTypography(Typography.R12)
        setTextColor(AppColorProvider.priceMuted)
        applySingleLine()
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private val priceColumn = AppVerticalLinearLayout(context).apply {
        gravity = Gravity.CENTER_VERTICAL
        addView(priceView, AppLayout.Linear.wrapContent())
        addView(oldPriceView, AppLayout.Linear.wrapContent())
    }

    private val actionButton = AppButtonView(context)

    private val bottomBar = AppHorizontalLinearLayout(context).apply {
        gravity = Gravity.CENTER_VERTICAL
        setPadding(horizontal = EDGE_DP.dp, vertical = 10.dp)
        addView(priceColumn, AppLayout.Linear.wrapContent().margin(end = 16.dp))
        addView(
            actionButton,
            AppLayout.Linear.get(0, AppLayout.WRAP).apply { weight = 1F }
        )
    }

    private val contentContainer = AppVerticalLinearLayout(context).apply {
        addView(scrollView, AppLayout.Linear.availableHeightParams())
        addView(bottomBar, AppLayout.Linear.defaultParams())
    }

    init {
        setContent(contentContainer)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun renderSuccess(model: ProductDetailUiModel) {
        val product = model.product

        imageView.load(product.image) {
            crossfade(true)
            transformations(RoundedCornersTransformation(IMAGE_CORNER_RADIUS_DP.dp.toFloat()))
        }
        titleView.text = product.title
        makerView.text = listOf(product.developer, product.publisher)
            .filter { it.isNotBlank() }
            .distinct()
            .joinToString(" · ")

        bindRating(product)
        bindTags(product)
        bindFacts(product)
        bindPrice(product)
        bindAction(model)

        descriptionView.text = product.description
    }

    @SuppressLint("SetTextI18n")
    private fun bindRating(product: Product) {
        val rating = product.ratingAvg
        val count = product.ratingCount ?: 0
        ratingRow.isVisible = rating != null
        if (rating != null) {
            ratingText.text = String.format(Locale.getDefault(), "%.1f", rating)
            ratingCountText.text = "($count)"
            ratingCountText.isVisible = count > 0
        }
    }

    private fun bindTags(product: Product) {
        tagsRow.removeAllViews()
        val tags = buildList {
            if (product.genre.isNotBlank()) add(product.genre)
            addAll(product.platforms.map { it.platformLabel() })
        }
        tags.forEachIndexed { index, tag ->
            tagsRow.addView(
                buildTagChip(text = tag, isHighlighted = index == 0),
                AppLayout.Linear.wrapContent().margin(end = 8.dp)
            )
        }
        tagsScroll.isVisible = tags.isNotEmpty()
    }

    private fun bindFacts(product: Product) {
        releaseDateRow.value = formatDate(product.releaseDate)

        val stock = product.stock
        stockRow.isVisible = stock != null
        if (stock != null) {
            when {
                stock <= 0 -> stockRow.setValue("Out of stock", AppColorProvider.discountBadge)
                stock < LOW_STOCK_THRESHOLD -> stockRow.setValue(
                    "Only $stock left",
                    AppColorProvider.discountBadge,
                )
                else -> stockRow.setValue("In stock", AppColorProvider.gray)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindPrice(product: Product) {
        priceView.text = formatPrice(product.currentPrice, product.currency)

        val oldPrice = product.oldPrice
        val hasDiscount = oldPrice != null && oldPrice > product.currentPrice
        oldPriceView.isVisible = hasDiscount
        discountBadge.isVisible = hasDiscount
        if (hasDiscount) {
            oldPriceView.text = formatPrice(oldPrice, product.currency)
            val percent = (((oldPrice - product.currentPrice) / oldPrice) * 100).roundToInt()
            discountBadge.text = "-$percent%"
            discountBadge.isVisible = percent > 0
        }
    }

    private fun bindAction(model: ProductDetailUiModel) {
        when {
            model.isOutOfStock -> {
                actionButton.text = "Out of Stock"
                actionButton.isEnabled = false
                actionButton.background = DrawableBuilder(context)
                    .solidColor(AppColorProvider.imagePlaceholder)
                    .cornerRadius(12.dp)
                    .build()
                actionButton.setOnClickListener(null)
            }
            model.isInCart -> {
                actionButton.text = "Go to Cart"
                actionButton.isEnabled = true
                actionButton.background = DrawableBuilder(context)
                    .solidColor(AppColorProvider.buttonFilled)
                    .cornerRadius(12.dp)
                    .build()
                actionButton.setOnClickListener { onGoToCartClickListener.onClick(it) }
            }
            else -> {
                actionButton.text = "Add to Cart"
                actionButton.isEnabled = true
                actionButton.background = DrawableBuilder(context)
                    .solidColor(AppColorProvider.buttonFilled)
                    .cornerRadius(12.dp)
                    .build()
                actionButton.setOnClickListener { onAddToCartClickListener.onClick(it) }
            }
        }
    }

    private fun formatDate(releaseDate: String): String = runCatching {
        LocalDate.parse(releaseDate).format(
            DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())
        )
    }.getOrDefault(releaseDate)

    @SuppressLint("DefaultLocale")
    private fun formatPrice(value: Double, currency: String): String {
        val rendered = if (value % 1.0 == 0.0) value.toInt().toString()
        else String.format("%.2f", value)
        return "$rendered $currency"
    }

    private fun String.platformLabel(): String =
        replace('_', ' ').lowercase().split(' ').joinToString(" ") { word ->
            word.replaceFirstChar { it.uppercase() }
        }.replace("Pc", "PC").replace("Ps", "PS")

    private fun buildTagChip(text: String, isHighlighted: Boolean): AppTextView =
        AppTextView(context).apply {
            setTypography(Typography.M12)
            setTextColor(if (isHighlighted) AppColorProvider.alwaysWhite else AppColorProvider.black)
            setPadding(horizontal = 12.dp, vertical = 6.dp)
            applySingleLine()
            background = DrawableBuilder(context)
                .solidColor(
                    if (isHighlighted) AppColorProvider.primaryMain
                    else AppColorProvider.imagePlaceholder
                )
                .cornerRadius(999.dp)
                .build()
            this.text = text
        }

    private class FactRow(context: Context, label: String) : AppHorizontalLinearLayout(context) {

        private val valueTextView = AppTextView(context).apply {
            setTypography(Typography.M14)
            setTextColor(AppColorProvider.black)
            applySingleLine()
        }

        var value: String
            get() = valueTextView.text.toString()
            set(newValue) {
                valueTextView.text = newValue
            }

        fun setValue(text: String, color: com.shopverse.android.core.color.AppColor) {
            valueTextView.text = text
            valueTextView.setTextColor(color)
        }

        init {
            minimumHeight = FACT_ROW_HEIGHT_DP.dp
            gravity = Gravity.CENTER_VERTICAL
            setPadding(horizontal = SIDE_PADDING_DP.dp)
            addView(
                AppTextView(context).apply {
                    setTypography(Typography.R14)
                    setTextColor(AppColorProvider.gray)
                    applySingleLine()
                    text = label
                },
                AppLayout.Linear.availableWidthParams().margin(end = SIDE_PADDING_DP.dp)
            )
            addView(valueTextView, AppLayout.Linear.wrapContent())
        }
    }

    companion object {
        private const val IMAGE_HEIGHT_DP = 400
        private const val IMAGE_CORNER_RADIUS_DP = 14
        private const val CARD_CORNER_RADIUS_DP = 18
        private const val FACT_ROW_HEIGHT_DP = 48
        private const val SIDE_PADDING_DP = 16
        private const val EDGE_DP = 12
        private const val LOW_STOCK_THRESHOLD = 10
    }
}
