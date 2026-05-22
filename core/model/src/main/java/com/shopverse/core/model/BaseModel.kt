package com.shopverse.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class BaseModel : Parcelable {

    abstract val meta: Meta

    @Parcelize
    data class MetaOnly(override val meta: Meta) : BaseModel()

    @Parcelize
    data class Data<T>(override val meta: Meta, val data: @RawValue T) : BaseModel()
}
