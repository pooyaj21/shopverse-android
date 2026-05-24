package com.shopverse.android.presentation.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Source : Parcelable {
    @Parcelize data class DeepLink(val deeplink: String) : Source()
    @Parcelize data object Splash : Source()
    @Parcelize object Navigator : Source()
    @Parcelize data object Onboarding : Source()
    @Parcelize data object Home : Source()
    @Parcelize data object Cart : Source()
}