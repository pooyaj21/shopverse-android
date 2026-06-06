package com.shopverse.android.core.extension

import androidx.appcompat.app.AppCompatDelegate
import com.shopverse.core.model.ThemeMode

val ThemeMode.label: String
    get() = when (this) {
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
        ThemeMode.SYSTEM -> "System"
    }

fun ThemeMode.applyNightMode() {
    AppCompatDelegate.setDefaultNightMode(
        when (this) {
            ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}
