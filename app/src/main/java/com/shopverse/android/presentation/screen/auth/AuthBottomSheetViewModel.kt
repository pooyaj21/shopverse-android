package com.shopverse.android.presentation.screen.auth

import androidx.lifecycle.viewModelScope
import com.shopverse.android.presentation.architecture.BaseViewModelState
import com.shopverse.android.presentation.architecture.ViewState
import com.shopverse.core.service.api.AuthService
import com.shopverse.core.shared.AppResult
import kotlinx.coroutines.launch

class AuthBottomSheetViewModel(
    private val authService: AuthService,
) : BaseViewModelState<AuthBottomSheetUiModel, AuthBottomSheetViewModel.Effect>(
    initialState = ViewState.Success(
        AuthBottomSheetUiModel(mode = AuthMode.Login, isSubmitting = false)
    )
) {

    private val currentModel: AuthBottomSheetUiModel
        get() = (state as? ViewState.Success)?.model
            ?: AuthBottomSheetUiModel(mode = AuthMode.Login, isSubmitting = false)

    fun switchMode() {
        if (currentModel.isSubmitting) return
        val next = when (currentModel.mode) {
            AuthMode.Login -> AuthMode.Register
            AuthMode.Register -> AuthMode.Login
        }
        viewModelScope.launch { setSuccessState(currentModel.copy(mode = next)) }
    }

    fun submit(name: String, email: String, password: String) {
        if (currentModel.isSubmitting) return
        when (val validation = validate(currentModel.mode, name, email, password)) {
            is Validation.Invalid -> {
                viewModelScope.launch { sendEffect(Effect.ShowMessage(validation.message)) }
                return
            }
            Validation.Valid -> Unit
        }
        viewModelScope.launch {
            setSuccessState(currentModel.copy(isSubmitting = true))
            val result = when (currentModel.mode) {
                AuthMode.Login -> authService.login(email = email, password = password)
                AuthMode.Register -> authService.signUp(
                    name = name,
                    email = email,
                    password = password,
                )
            }
            setSuccessState(currentModel.copy(isSubmitting = false))
            when (result) {
                is AppResult.Success -> sendEffect(Effect.AuthCompleted)
                is AppResult.Error.Local -> sendEffect(
                    Effect.ShowMessage("Network problem. Please try again.")
                )
                is AppResult.Error.Remote -> sendEffect(
                    Effect.ShowMessage(prettyRemoteError(result.httpCode, result.message))
                )
            }
        }
    }

    private fun validate(
        mode: AuthMode,
        name: String,
        email: String,
        password: String,
    ): Validation {
        if (mode == AuthMode.Register && name.isBlank()) {
            return Validation.Invalid("Please enter your name.")
        }
        if (email.isBlank() || !email.contains('@')) {
            return Validation.Invalid("Please enter a valid email.")
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            return Validation.Invalid("Password must be at least $MIN_PASSWORD_LENGTH characters.")
        }
        return Validation.Valid
    }

    private fun prettyRemoteError(httpCode: Int, message: String?): String {
        if (message.isNullOrBlank()) return "Something went wrong ($httpCode)."
        return when {
            message.contains("invalid_grant", ignoreCase = true) -> "Wrong email or password."
            message.contains("user_already_exists", ignoreCase = true) ->
                "An account with this email already exists."
            message.contains("weak_password", ignoreCase = true) ->
                "Password is too weak. Use at least 6 characters."
            else -> "Something went wrong ($httpCode)."
        }
    }

    private sealed class Validation {
        data object Valid : Validation()
        data class Invalid(val message: String) : Validation()
    }

    sealed class Effect {
        data class ShowMessage(val message: String) : Effect()
        data object AuthCompleted : Effect()
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}
