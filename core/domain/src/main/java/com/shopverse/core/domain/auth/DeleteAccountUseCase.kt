package com.shopverse.core.domain.auth

import com.shopverse.core.data.auth.AuthRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.shared.AppResult

class DeleteAccountUseCase(
    private val authRepository: AuthRepository,
) : UseCase {
    suspend operator fun invoke(): AppResult<Unit> = authRepository.deleteAccount()
}
