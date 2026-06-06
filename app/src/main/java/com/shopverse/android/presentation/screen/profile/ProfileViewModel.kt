package com.shopverse.android.presentation.screen.profile

import androidx.lifecycle.viewModelScope
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.android.presentation.screen.profile.core.ProfileUiModel
import com.shopverse.core.domain.auth.GetSavedProfileUseCase
import com.shopverse.core.domain.auth.LogoutUseCase
import com.shopverse.core.domain.theme.GetThemeModeUseCase
import com.shopverse.core.domain.theme.SetThemeModeUseCase
import com.shopverse.core.model.ThemeMode
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getSavedProfileUseCase: GetSavedProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val appVersion: String,
    private val appBuildNumber: String,
) : BaseViewModelState<ProfileUiModel, Unit>() {

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            setSuccessState(
                ProfileUiModel.create(
                    isLoggedIn = getSavedProfileUseCase() != null,
                    themeMode = getThemeModeUseCase(),
                    appVersion = appVersion,
                    appBuildNumber = appBuildNumber,
                )
            )
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        setThemeModeUseCase(mode)
        refresh()
    }

    fun logout() {
        logoutUseCase()
        refresh()
    }
}
