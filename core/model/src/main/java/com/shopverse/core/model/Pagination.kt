package com.shopverse.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pagination(
    val hasMoreItems: Boolean,
    val cursor: String?,
    val totalItems: Int
) : Parcelable
