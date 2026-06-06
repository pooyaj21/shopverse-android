package com.shopverse.core.data.theme

import com.shopverse.core.model.ThemeMode
import com.shopverse.core.preferences.SharedPref

interface ThemeRepository {
    fun getThemeMode(): ThemeMode
    fun setThemeMode(mode: ThemeMode)
}

private const val KEY_THEME_MODE = "app_theme_mode"

class ThemeRepositoryImpl(
    private val sharedPref: SharedPref,
) : ThemeRepository {

    override fun getThemeMode(): ThemeMode {
        val saved = sharedPref.read(KEY_THEME_MODE, null) ?: return ThemeMode.SYSTEM
        return ThemeMode.entries.firstOrNull { it.name == saved } ?: ThemeMode.SYSTEM
    }

    override fun setThemeMode(mode: ThemeMode) {
        sharedPref.write(KEY_THEME_MODE, mode.name)
    }
}
