package com.shopverse.core.service.api.dto

import com.shopverse.core.model.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id") val id: String,
    @SerialName("slug") val slug: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("developer") val developer: String,
    @SerialName("publisher") val publisher: String,
    @SerialName("genre") val genre: String,
    @SerialName("platforms") val platforms: List<String>,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("current_price") val currentPrice: Double,
    @SerialName("old_price") val oldPrice: Double?,
    @SerialName("currency") val currency: String,
    @SerialName("cover_image_url") val image: String,
    @SerialName("stock") val stock: Int?,
    @SerialName("rating_avg") val ratingAvg: Double?,
    @SerialName("rating_count") val ratingCount: Int?,
)

fun ProductDto.toDomain(): Product = Product(
    id = id,
    slug = slug,
    title = title,
    description = description,
    developer = developer,
    publisher = publisher,
    genre = genre,
    platforms = platforms,
    releaseDate = releaseDate,
    currentPrice = currentPrice,
    oldPrice = oldPrice,
    currency = currency,
    image = image,
    stock = stock,
    ratingAvg = ratingAvg,
    ratingCount = ratingCount,
)
