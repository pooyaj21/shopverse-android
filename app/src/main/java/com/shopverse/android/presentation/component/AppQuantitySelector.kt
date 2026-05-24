package com.shopverse.android.presentation.component

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.setPadding
import com.shopverse.android.R
import com.shopverse.android.core.animation.AnimationType
import com.shopverse.android.core.animation.addAnimate
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
import com.shopverse.android.core.extension.dp
import com.shopverse.core.model.Product
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

@SuppressLint("ViewConstructor")
class AppQuantitySelector(
    context: Context,
    private val onAddToCartClickListener: (Product) -> Unit,
    private val onCartClickListener: () -> Unit
) : AppCompatImageView(context), KoinComponent {
    private val cartManager: CartManager by inject()


    init {
        background = DrawableBuilder(context)
            .oval()
            .solidColor(AppColorProvider.buttonFilled)
            .build()
        setPadding(8.dp)
        addAnimate(AnimationType.Scale(0.92F))
    }

    fun bind(product: Product) {
        bindAction(product)
    }

    private fun bindAction(product: Product) {
        val isInCart = cartManager.isProductInCart(product)
        setImageResource(if (isInCart) R.drawable.ic_cart else R.drawable.ic_add)
        imageTintList = android.content.res.ColorStateList.valueOf(
            AppColorProvider.alwaysWhite.value(context)
        )
        setOnClickListener {
            if (isInCart) onCartClickListener() else onAddToCartClickListener(product)
        }
    }
}