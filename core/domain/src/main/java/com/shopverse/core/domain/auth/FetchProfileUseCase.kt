package com.shopverse.core.domain.auth

import com.shopverse.core.data.auth.AuthRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.UserProfile
import com.shopverse.core.shared.AppResult

class FetchProfileUseCase(
    private val authRepository: AuthRepository,
) : UseCase {
    suspend operator fun invoke(): AppResult<UserProfile> = authRepository.fetchProfile()
}
