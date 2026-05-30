package com.shopverse.android.presentation.screen.profile

import androidx.lifecycle.viewModelScope
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.android.presentation.screen.profile.core.ProfileUiModel
import com.shopverse.core.domain.auth.GetSavedProfileUseCase
import com.shopverse.core.domain.auth.LogoutUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getSavedProfileUseCase: GetSavedProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
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
                    appVersion = appVersion,
                    appBuildNumber = appBuildNumber,
                )
            )
        }
    }

    fun logout() {
        logoutUseCase()
        refresh()
    }
}
