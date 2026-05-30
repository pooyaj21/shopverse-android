package com.shopverse.core.data.auth

import com.shopverse.core.model.UserProfile
import com.shopverse.core.preferences.SharedPref
import com.shopverse.core.service.api.AuthService
import com.shopverse.core.service.api.dto.AuthSessionDto
import com.shopverse.core.shared.AppResult
import com.shopverse.core.shared.mapIfSuccess

interface AuthRepository {
    suspend fun login(email: String, password: String): AppResult<UserProfile>
    suspend fun signUp(name: String, email: String, password: String): AppResult<UserProfile>
    fun getSavedProfile(): UserProfile?
    fun getAccessToken(): String?
    fun logout()
}

private const val KEY_PROFILE_ID = "auth_profile_id"
private const val KEY_PROFILE_NAME = "auth_profile_name"
private const val KEY_PROFILE_EMAIL = "auth_profile_email"
private const val KEY_ACCESS_TOKEN = "auth_access_token"
private const val KEY_REFRESH_TOKEN = "auth_refresh_token"

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val sharedPref: SharedPref,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<UserProfile> =
        authService.login(email = email, password = password)
            .also { it.persistTokens() }
            .mapIfSuccess { it.toProfileAndStoreIfAbsent() }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): AppResult<UserProfile> =
        authService.signUp(name = name, email = email, password = password)
            .also { it.persistTokens() }
            .mapIfSuccess { it.toProfileAndStoreIfAbsent() }

    override fun getSavedProfile(): UserProfile? {
        val id = sharedPref.read(KEY_PROFILE_ID, null) ?: return null
        return UserProfile(
            id = id,
            name = sharedPref.read(KEY_PROFILE_NAME, null),
            email = sharedPref.read(KEY_PROFILE_EMAIL, null),
        )
    }

    override fun getAccessToken(): String? = sharedPref.read(KEY_ACCESS_TOKEN, null)

    override fun logout() {
        sharedPref.remove(KEY_ACCESS_TOKEN)
        sharedPref.remove(KEY_REFRESH_TOKEN)
        sharedPref.remove(KEY_PROFILE_ID)
        sharedPref.remove(KEY_PROFILE_NAME)
        sharedPref.remove(KEY_PROFILE_EMAIL)
    }

    private fun AppResult<AuthSessionDto>.persistTokens() {
        val dto = (this as? AppResult.Success)?.value ?: return
        sharedPref.write(KEY_ACCESS_TOKEN, dto.accessToken)
        dto.refreshToken?.let { sharedPref.write(KEY_REFRESH_TOKEN, it) }
    }

    private fun AuthSessionDto.toProfileAndStoreIfAbsent(): UserProfile {
        val user = checkNotNull(user) { "Supabase auth response missing user" }
        val profile = UserProfile(
            id = user.id,
            name = user.metadata?.name,
            email = user.email,
        )
        if (sharedPref.read(KEY_PROFILE_ID, null) == null) {
            sharedPref.write(KEY_PROFILE_ID, profile.id)
            profile.name?.let { sharedPref.write(KEY_PROFILE_NAME, it) }
            profile.email?.let { sharedPref.write(KEY_PROFILE_EMAIL, it) }
        }
        return profile
    }
}
