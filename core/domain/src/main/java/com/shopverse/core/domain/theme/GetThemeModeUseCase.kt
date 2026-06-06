package com.shopverse.core.domain.theme

import com.shopverse.core.data.theme.ThemeRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.ThemeMode

class GetThemeModeUseCase(
    private val themeRepository: ThemeRepository,
) : UseCase {

    operator fun invoke(): ThemeMode = themeRepository.getThemeMode()
}
