package com.shopverse.core.model

data class OrderDetail(
    val id: String,
    val placedAt: String,
    val total: Double,
    val originalTotal: Double?,
    val currency: String,
    val items: List<OrderLineItem>,
)

data class OrderLineItem(
    val id: String,
    val productId: String?,
    val productSlug: String,
    val productTitle: String,
    // Live catalog image — null when the product was deleted after purchase.
    val productImage: String?,
    val unitPrice: Double,
    val quantity: Int,
    val lineTotal: Double,
)
