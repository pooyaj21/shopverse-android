package com.shopverse.core.domain.auth

import com.shopverse.core.data.auth.AuthRepository
import com.shopverse.core.domain.UseCase
import com.shopverse.core.model.UserProfile

class GetSavedProfileUseCase(
    private val authRepository: AuthRepository,
) : UseCase {
    operator fun invoke(): UserProfile? = authRepository.getSavedProfile()
}
