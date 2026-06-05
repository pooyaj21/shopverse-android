package com.shopverse.core.data.auth

import com.shopverse.core.model.UserProfile
import com.shopverse.core.preferences.SharedPref
import com.shopverse.core.service.api.AuthService
import com.shopverse.core.service.api.dto.AuthSessionDto
import com.shopverse.core.service.supabase.SessionTokenStore
import com.shopverse.core.shared.AppResult

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

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val sharedPref: SharedPref,
    private val tokenStore: SessionTokenStore,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AppResult<UserProfile> =
        persistAndMap(authService.login(email = email, password = password))

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): AppResult<UserProfile> =
        persistAndMap(authService.signUp(name = name, email = email, password = password))

    override fun getSavedProfile(): UserProfile? {
        if (getAccessToken().isNullOrBlank()) return null
        val id = sharedPref.read(KEY_PROFILE_ID, null) ?: return null
        return UserProfile(
            id = id,
            name = sharedPref.read(KEY_PROFILE_NAME, null),
            email = sharedPref.read(KEY_PROFILE_EMAIL, null),
        )
    }

    override fun getAccessToken(): String? = tokenStore.accessToken

    override fun logout() {
        tokenStore.clear()
        sharedPref.remove(KEY_PROFILE_ID)
        sharedPref.remove(KEY_PROFILE_NAME)
        sharedPref.remove(KEY_PROFILE_EMAIL)
    }

    private fun persistAndMap(result: AppResult<AuthSessionDto>): AppResult<UserProfile> =
        when (result) {
            is AppResult.Success -> {
                val dto = result.value
                val accessToken = dto.accessToken
                val user = dto.user
                if (accessToken.isNullOrBlank() || user == null) {
                    // Supabase returned no session (e.g. signup that requires email confirmation).
                    // Treat as a remote error so the caller can surface a clear message.
                    AppResult.Error.Remote(
                        httpCode = 200,
                        message = "email_confirmation_required",
                        cause = null,
                    )
                } else {
                    persistSession(accessToken, dto.refreshToken, user.id, user.metadata?.name, user.email)
                    AppResult.Success(
                        UserProfile(id = user.id, name = user.metadata?.name, email = user.email)
                    )
                }
            }
            is AppResult.Error.Local -> result
            is AppResult.Error.Remote -> result
        }

    private fun persistSession(
        accessToken: String,
        refreshToken: String?,
        userId: String,
        userName: String?,
        userEmail: String?,
    ) {
        tokenStore.update(accessToken = accessToken, refreshToken = refreshToken)
        sharedPref.write(KEY_PROFILE_ID, userId)
        if (userName != null) sharedPref.write(KEY_PROFILE_NAME, userName)
        if (userEmail != null) sharedPref.write(KEY_PROFILE_EMAIL, userEmail)
    }
}
