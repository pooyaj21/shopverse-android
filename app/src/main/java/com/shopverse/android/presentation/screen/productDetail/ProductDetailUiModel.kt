package com.shopverse.android.presentation.screen.productDetail

import com.shopverse.core.model.Product

data class ProductDetailUiModel(
    val product: Product,
    val isInCart: Boolean,
) {
    val isOutOfStock: Boolean get() = product.stock != null && product.stock!! <= 0
}
