package com.shopverse.core.model

data class LocalCartItem(
    val id: String,
    val slug: String,
    val title: String,
    val currentPrice: Double,
    val oldPrice: Double?,
    val currency: String,
    val image: String,
    val count: Int,
)
