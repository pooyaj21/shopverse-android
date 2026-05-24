package com.shopverse.android.presentation.feature.prodcutList

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import coil.load
import coil.transform.RoundedCornersTransformation
import com.shopverse.android.R
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
import com.shopverse.android.core.extension.applyMaxLine
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
import com.shopverse.android.presentation.component.AppQuantitySelector
import com.shopverse.android.presentation.component.AppTextView
import com.shopverse.core.model.Product
import kotlin.math.roundToInt

@SuppressLint("ViewConstructor")
class ProductCellView(
    context: Context,
    private val onAddToCartClickListener: (Product) -> Unit,
    private val onCartClickListener: () -> Unit
) : AppVerticalLinearLayout(context) {

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

    private val actionButton = AppQuantitySelector(
        context = context,
        onAddToCartClickListener = onAddToCartClickListener,
        onCartClickListener = onCartClickListener
    )

    private val imageContainer = FrameLayout(context).apply {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.imagePlaceholder)
            .cornerRadius(IMAGE_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        addView(imageView, AppLayout.Frame.get(AppLayout.MATCH, 175.dp))
        addView(
            discountBadge,
            AppLayout.Frame.wrapContent()
                .gravity(Gravity.TOP or Gravity.START)
                .margin(start = 8.dp, top = 8.dp)
        )
        addView(
            actionButton,
            AppLayout.Frame.get(ACTION_BUTTON_SIZE_DP.dp)
                .gravity(Gravity.BOTTOM or Gravity.END)
                .margin(end = 8.dp, bottom = 8.dp)
        )
    }

    private val titleView = AppTextView(context).apply {
        setTypography(Typography.M14)
        setTextColor(AppColorProvider.black)
        applyMaxLine(2)
    }

    private val priceView = AppTextView(context).apply {
        setTypography(Typography.SB16)
        setTextColor(AppColorProvider.black)
        applySingleLine()
    }

    private val oldPriceView = AppTextView(context).apply {
        setTypography(Typography.R12)
        setTextColor(AppColorProvider.priceMuted)
        applySingleLine()
        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private val priceRow = AppHorizontalLinearLayout(context).apply {
        gravity = Gravity.CENTER_VERTICAL
        addView(priceView, AppLayout.Linear.wrapContent())
        addView(
            oldPriceView,
            AppLayout.Linear.wrapContent().margin(start = 6.dp)
        )
    }

    private val ratingIcon = ImageView(context).apply {
        setImageResource(R.drawable.ic_star)
        imageTintList = ColorStateList.valueOf(
            AppColorProvider.ratingStar.value(context)
        )
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

    init {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.cardSurface)
            .cornerRadius(CARD_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        setPadding(10.dp)
        addView(imageContainer, AppLayout.Linear.get(MATCH, 0).apply { weight = 1F })
        addView(
            titleView,
            AppLayout.Linear.defaultParams().margin(top = 10.dp)
        )
        addView(
            priceRow,
            AppLayout.Linear.defaultParams().margin(top = 6.dp)
        )
        addView(
            ratingRow,
            AppLayout.Linear.defaultParams().margin(top = 6.dp)
        )
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    fun bind(product: Product) {
        imageView.load(product.image) {
            crossfade(true)
            transformations(RoundedCornersTransformation(IMAGE_CORNER_RADIUS_DP.dp.toFloat()))
        }
        titleView.text = product.title
        priceView.text = formatPrice(product.currentPrice, product.currency)

        val oldPrice = product.oldPrice
        val hasDiscount = oldPrice != null && oldPrice > product.currentPrice
        oldPriceView.isVisible = hasDiscount
        if (hasDiscount) {
            oldPriceView.text = formatPrice(oldPrice, product.currency)
            val percent = (((oldPrice - product.currentPrice) / oldPrice) * 100).roundToInt()
            discountBadge.text = "-$percent%"
            discountBadge.isVisible = percent > 0
        } else {
            discountBadge.isVisible = false
        }

        val rating = product.ratingAvg
        val count = product.ratingCount ?: 0
        if (rating != null) {
            ratingRow.isVisible = true
            ratingText.text = String.format("%.1f", rating)
            ratingCountText.text = if (count > 0) "($count)" else ""
            ratingCountText.isVisible = count > 0
        } else {
            ratingRow.isVisible = false
        }
        actionButton.bind(product)
    }


    @SuppressLint("DefaultLocale")
    private fun formatPrice(value: Double, currency: String): String {
        val rendered = if (value % 1.0 == 0.0) value.toInt().toString()
        else String.format("%.2f", value)
        return "$rendered $currency"
    }

    companion object {
        private const val IMAGE_CORNER_RADIUS_DP = 14
        private const val CARD_CORNER_RADIUS_DP = 18
        private const val ACTION_BUTTON_SIZE_DP = 36
        private const val MATCH = AppLayout.MATCH
    }
}