package com.shopverse.core.service.api.dto

import com.shopverse.core.model.OrderDetail
import com.shopverse.core.model.OrderLineItem
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

@Serializable
data class OrderDetailDto(
    @SerialName("id") val id: String,
    @SerialName("placed_at") val placedAt: String,
    @SerialName("total") val total: Double,
    @SerialName("original_total") val originalTotal: Double?,
    @SerialName("currency") val currency: String,
    @SerialName("order_items") val items: List<OrderItemDto> = emptyList(),
)

@Serializable
data class OrderItemDto(
    @SerialName("id") val id: String,
    @SerialName("product_id") val productId: String? = null,
    @SerialName("product_slug") val productSlug: String,
    @SerialName("product_title") val productTitle: String,
    @SerialName("unit_price") val unitPrice: Double,
    @SerialName("quantity") val quantity: Int,
    @SerialName("line_total") val lineTotal: Double,
    // Embedded catalog row — null when the product was deleted after purchase.
    @SerialName("products") val product: EmbeddedProductDto? = null,
) {
    @Serializable
    data class EmbeddedProductDto(
        @SerialName("cover_image_url") val image: String? = null,
    )
}

fun OrderDetailDto.toDomain(): OrderDetail = OrderDetail(
    id = id,
    placedAt = placedAt,
    total = total,
    originalTotal = originalTotal,
    currency = currency,
    items = items.map { it.toDomain() },
)

fun OrderItemDto.toDomain(): OrderLineItem = OrderLineItem(
    id = id,
    productId = productId,
    productSlug = productSlug,
    productTitle = productTitle,
    productImage = product?.image,
    unitPrice = unitPrice,
    quantity = quantity,
    lineTotal = lineTotal,
)
