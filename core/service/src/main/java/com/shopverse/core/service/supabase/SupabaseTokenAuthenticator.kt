package com.shopverse.core.service.supabase

import com.shopverse.core.service.api.dto.AuthSessionDto
import com.shopverse.core.service.api.dto.RefreshTokenRequestDto
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route

private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

/**
 * Reacts to 401 responses by exchanging the stored refresh token for a fresh
 * access token, then retrying the failed request once with the new bearer.
 *
 * Supabase access tokens expire after one hour, so any session older than that
 * (e.g. the user comes back days later) 401s until refreshed. Refresh tokens
 * rotate on every use, which is why the refresh is single-flight: concurrent
 * 401s must not each spend the (single-use) refresh token.
 */
class SupabaseTokenAuthenticator(
    private val config: SupabaseConfig,
    private val tokenStore: SessionTokenStore,
    private val json: Json,
) : Authenticator {

    // Bare client on purpose — the refresh call must not recurse into this
    // authenticator when the refresh token itself is rejected with a 401.
    private val refreshClient = OkHttpClient()
    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        val failedBearer = response.request.header("Authorization")
            ?.removePrefix("Bearer ")
            ?: return null

        // Anon-key requests carry no session to refresh.
        if (failedBearer == config.anonKey) return null

        // The retried request 401'd too — a fresh token didn't help, give up.
        if (responseCount(response) >= MAX_ATTEMPTS) return null

        synchronized(lock) {
            // Another thread may have refreshed while we waited on the lock.
            val current = tokenStore.accessToken ?: return null
            if (current != failedBearer) return response.request.withBearer(current)

            val refreshToken = tokenStore.refreshToken ?: return null
            return when (val result = refresh(refreshToken)) {
                is RefreshResult.Success -> {
                    tokenStore.update(
                        accessToken = result.accessToken,
                        refreshToken = result.refreshToken,
                    )
                    response.request.withBearer(result.accessToken)
                }
                RefreshResult.Rejected -> {
                    // The refresh token is expired/revoked — the session is
                    // gone for good. Drop it so the app falls back to the
                    // logged-out flow instead of retrying forever.
                    tokenStore.clear()
                    null
                }
                // Transient problem (offline, 5xx): keep the session and let
                // the original 401 surface; a later call will retry the refresh.
                RefreshResult.Unreachable -> null
            }
        }
    }

    private fun refresh(refreshToken: String): RefreshResult = try {
        val payload = json.encodeToString(
            RefreshTokenRequestDto.serializer(),
            RefreshTokenRequestDto(refreshToken = refreshToken),
        )
        val request = Request.Builder()
            .url("${config.authUrl}/token?grant_type=refresh_token")
            .header("apikey", config.anonKey)
            .header("Accept", "application/json")
            .post(payload.toRequestBody(JSON_MEDIA_TYPE))
            .build()
        refreshClient.newCall(request).execute().use { refreshResponse ->
            when {
                refreshResponse.isSuccessful -> {
                    val session = json.decodeFromString(
                        AuthSessionDto.serializer(),
                        refreshResponse.body?.string().orEmpty(),
                    )
                    val accessToken = session.accessToken
                    if (accessToken.isNullOrBlank()) RefreshResult.Unreachable
                    else RefreshResult.Success(accessToken, session.refreshToken)
                }
                refreshResponse.code in 400..499 -> RefreshResult.Rejected
                else -> RefreshResult.Unreachable
            }
        }
    } catch (t: Throwable) {
        RefreshResult.Unreachable
    }

    private fun Request.withBearer(token: String): Request =
        newBuilder().header("Authorization", "Bearer $token").build()

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

    private sealed interface RefreshResult {
        data class Success(val accessToken: String, val refreshToken: String?) : RefreshResult
        data object Rejected : RefreshResult
        data object Unreachable : RefreshResult
    }

    companion object {
        private const val MAX_ATTEMPTS = 2
    }
}
