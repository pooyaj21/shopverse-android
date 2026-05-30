package com.shopverse.core.service.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitOrderRequestDto(
    @SerialName("items") val items: List<SubmitOrderItemDto>,
)

@Serializable
data class SubmitOrderItemDto(
    @SerialName("productId") val productId: String,
    @SerialName("quantity") val quantity: Int,
)

@Serializable
data class SubmitOrderEnvelopeDto(
    @SerialName("data") val data: SubmittedOrderDto? = null,
    @SerialName("meta") val meta: SubmitOrderMetaDto? = null,
)

@Serializable
data class SubmitOrderMetaDto(
    @SerialName("code") val code: Int? = null,
    @SerialName("msg") val msg: String? = null,
)

@Serializable
data class SubmittedOrderDto(
    @SerialName("id") val id: String,
    @SerialName("placedAt") val placedAt: String? = null,
)
