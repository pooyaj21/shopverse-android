package com.shopverse.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meta(
    val code: Int,
    val message: String?
) : Parcelable
