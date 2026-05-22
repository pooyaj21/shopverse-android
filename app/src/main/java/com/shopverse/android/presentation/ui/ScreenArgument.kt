package com.shopverse.android.presentation.ui

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import com.shopverse.android.BuildConfig
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

interface ScreenArgument<T> : Parcelable {
    val source: Source
    val requirements: T
}

val ScreenArgument<*>.isDeepLink: Boolean get() = (source is Source.DeepLink)

@Parcelize
data class NoRequirementArgs(
    override val source: Source
) : ScreenArgument<NoRequirementArgs.NoRequirement> {

    @IgnoredOnParcel
    override val requirements: NoRequirement = NoRequirement

    @Parcelize
    object NoRequirement : Parcelable
}

fun ScreenArgument<*>.toBundle(): Bundle = bundleOf(BuildConfig.SCREEN_ARGS to this)