package com.shopverse.core.service.api.dto

import com.shopverse.core.model.OrderSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderSummaryDto(
    @SerialName("id") val id: String,
    @SerialName("placed_at") val placedAt: String,
    @SerialName("total") val total: Double,
    @SerialName("original_total") val originalTotal: Double?,
    @SerialName("currency") val currency: String,
)

fun OrderSummaryDto.toDomain(): OrderSummary = OrderSummary(
    id = id,
    placedAt = placedAt,
    total = total,
    originalTotal = originalTotal,
    currency = currency,
)
