package com.shopverse.core.domain.auth

import com.shopverse.core.data.auth.AuthRepository
import com.shopverse.core.domain.UseCase

class LogoutUseCase(
    private val authRepository: AuthRepository,
) : UseCase {
    operator fun invoke() = authRepository.logout()
}
