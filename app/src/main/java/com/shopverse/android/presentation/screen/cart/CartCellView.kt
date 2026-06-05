package com.shopverse.android.presentation.screen.cart

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import coil.load
import coil.transform.RoundedCornersTransformation
import com.shopverse.android.R
import com.shopverse.android.core.animation.AnimationType
import com.shopverse.android.core.animation.addAnimate
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
import com.shopverse.android.core.extension.applyMaxLine
import com.shopverse.android.core.extension.applySingleLine
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppHorizontalLinearLayout
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.component.AppTextView
import com.shopverse.core.model.LocalCartItem

@SuppressLint("ViewConstructor")
class CartCellView(
    context: Context,
    private val onItemClickListener: (LocalCartItem) -> Unit,
    private val onRemoveClickListener: (LocalCartItem) -> Unit,
) : AppHorizontalLinearLayout(context) {

    private val imageView = ImageView(context).apply {
        scaleType = ImageView.ScaleType.FIT_CENTER
        setBackgroundColor(AppColorProvider.imagePlaceholder)
    }

    private val imageContainer = FrameLayout(context).apply {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.imagePlaceholder)
            .cornerRadius(IMAGE_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        addView(imageView, AppLayout.Frame.fullScreen())
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

    private val infoColumn = AppVerticalLinearLayout(context).apply {
        gravity = Gravity.CENTER_VERTICAL
        addView(titleView, AppLayout.Linear.defaultParams())
        addView(priceRow, AppLayout.Linear.defaultParams().margin(top = 6.dp))
    }

    private val removeButton = AppCompatImageView(context).apply {
        setImageResource(R.drawable.ic_trash)
        imageTintList = ColorStateList.valueOf(AppColorProvider.gray.value(context))
        background = DrawableBuilder(context)
            .oval()
            .solidColor(AppColorProvider.imagePlaceholder)
            .build()
        setPadding(9.dp)
        addAnimate(AnimationType.Scale(0.92F))
    }

    init {
        background = DrawableBuilder(context)
            .solidColor(AppColorProvider.cardSurface)
            .cornerRadius(CARD_CORNER_RADIUS_DP.dp)
            .build()
        clipToOutline = true
        gravity = Gravity.CENTER_VERTICAL
        setPadding(10.dp)
        addView(imageContainer, AppLayout.Linear.get(IMAGE_SIZE_DP.dp))
        addView(
            infoColumn,
            AppLayout.Linear.get(0, AppLayout.WRAP).apply {
                weight = 1F
            }.margin(start = 12.dp)
        )
        addView(
            removeButton,
            AppLayout.Linear.get(REMOVE_BUTTON_SIZE_DP.dp).margin(start = 12.dp)
        )
    }

    fun bind(item: LocalCartItem) {
        imageView.load(item.image) {
            crossfade(true)
            transformations(RoundedCornersTransformation(IMAGE_CORNER_RADIUS_DP.dp.toFloat()))
        }
        titleView.text = item.title
        priceView.text = formatPrice(item.currentPrice, item.currency)

        val oldPrice = item.oldPrice
        val hasDiscount = oldPrice != null && oldPrice > item.currentPrice
        oldPriceView.isVisible = hasDiscount
        if (hasDiscount) {
            oldPriceView.text = formatPrice(oldPrice, item.currency)
        }
        setOnClickListener { onItemClickListener(item) }
        removeButton.setOnClickListener { onRemoveClickListener(item) }
    }

    @SuppressLint("DefaultLocale")
    private fun formatPrice(value: Double, currency: String): String {
        val rendered = if (value % 1.0 == 0.0) value.toInt().toString()
        else String.format("%.2f", value)
        return "$rendered $currency"
    }

    companion object {
        private const val IMAGE_SIZE_DP = 84
        private const val IMAGE_CORNER_RADIUS_DP = 14
        private const val CARD_CORNER_RADIUS_DP = 18
        private const val REMOVE_BUTTON_SIZE_DP = 40
    }
}
