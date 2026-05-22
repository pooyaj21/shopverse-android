package com.shopverse.core.service.api.dto

import com.shopverse.core.model.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: String,
    val slug: String,
    val title: String,
    @SerialName("cover_image_url") val coverImageUrl: String? = null,
    @SerialName("current_price") val currentPrice: String? = null,
    val currency: String? = null,
)

fun ProductDto.toDomain(): Product = Product(
    id = id,
    slug = slug,
    title = title,
    coverImageUrl = coverImageUrl,
    currentPrice = currentPrice,
    currency = currency,
)
