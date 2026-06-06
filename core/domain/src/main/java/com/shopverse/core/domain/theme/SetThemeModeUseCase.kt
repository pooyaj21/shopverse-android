package com.shopverse.core.domain.theme

import com.shopverse.core.data.theme.ThemeRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.ThemeMode

class SetThemeModeUseCase(
    private val themeRepository: ThemeRepository,
) : UseCase {

    operator fun invoke(mode: ThemeMode) = themeRepository.setThemeMode(mode)
}
