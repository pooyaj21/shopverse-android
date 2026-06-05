package com.shopverse.android.presentation.screen.account

import androidx.lifecycle.viewModelScope
import com.shopverse.android.presentation.architecture.Alert
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.core.domain.auth.DeleteAccountUseCase
import com.shopverse.core.domain.auth.FetchProfileUseCase
import com.shopverse.core.domain.auth.GetSavedProfileUseCase
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.launch

class AccountViewModel(
    private val getSavedProfileUseCase: GetSavedProfileUseCase,
    private val fetchProfileUseCase: FetchProfileUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : BaseViewModelState<AccountUiModel, AccountEffect>() {

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val saved = getSavedProfileUseCase()
            if (saved == null) {
                setEmptyState()
                return@launch
            }
            setSuccessState(AccountUiModel(name = saved.name, email = saved.email))

            // Refresh from the server; keep showing the saved profile if it fails.
            val result = fetchProfileUseCase()
            if (result is AppResult.Success) {
                setSuccessState(
                    AccountUiModel(name = result.value.name, email = result.value.email)
                )
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            setLoadingState(onFrontOfContent = true)
            when (val result = deleteAccountUseCase()) {
                is AppResult.Success -> {
                    sendAlert(Alert.Success("Your account has been deleted."))
                    sendEffect(AccountEffect.AccountDeleted)
                }

                is AppResult.Error.Local -> {
                    refresh()
                    sendAlert(Alert.Error("Network problem. Please try again."))
                }

                is AppResult.Error.Remote -> {
                    refresh()
                    sendAlert(
                        Alert.Error(
                            if (result.httpCode == 401) "Please log in again to delete your account."
                            else "Couldn't delete your account (${result.httpCode})."
                        )
                    )
                }
            }
        }
    }


}
