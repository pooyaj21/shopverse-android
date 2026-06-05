package com.shopverse.core.model

data class OrderSummary(
    val id: String,
    val placedAt: String,
    val total: Double,
    val originalTotal: Double?,
    val currency: String,
)
