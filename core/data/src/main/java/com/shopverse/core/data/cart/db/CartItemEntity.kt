package com.shopverse.core.data.cart.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shopverse.core.model.LocalCartItem

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val id: String,
    val slug: String,
    val title: String,
    val currentPrice: Double,
    val oldPrice: Double?,
    val currency: String,
    val image: String,
    val count: Int,
    val updatedAt: Long,
)

fun CartItemEntity.toDomain(): LocalCartItem = LocalCartItem(
    id = id,
    slug = slug,
    title = title,
    currentPrice = currentPrice,
    oldPrice = oldPrice,
    currency = currency,
    image = image,
    count = count,
)

fun LocalCartItem.toEntity(updatedAt: Long = System.currentTimeMillis()): CartItemEntity =
    CartItemEntity(
        id = id,
        slug = slug,
        title = title,
        currentPrice = currentPrice,
        oldPrice = oldPrice,
        currency = currency,
        image = image,
        count = count,
        updatedAt = updatedAt,
    )
