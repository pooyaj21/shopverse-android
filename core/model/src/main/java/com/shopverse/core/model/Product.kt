package com.shopverse.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val slug: String,
    val title: String,
    val coverImageUrl: String? = null,
    val currentPrice: String? = null,
    val currency: String? = null,
) : Parcelable
