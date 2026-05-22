package com.shopverse.android.presentation.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Source : Parcelable {
    @Parcelize data class DeepLink(val deeplink: String) : Source()
    @Parcelize data object Splash : Source()
    @Parcelize data object Onboarding : Source()
    @Parcelize data object Home : Source()
}