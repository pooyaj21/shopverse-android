package com.shopverse.core.service.api

import com.shopverse.core.service.api.dto.AuthSessionDto
import com.shopverse.core.service.api.dto.LoginRequestDto
import com.shopverse.core.service.api.dto.SignUpRequestDto
import com.shopverse.core.service.supabase.SupabaseClient
import com.shopverse.core.shared.AppResult
import kotlinx.serialization.json.Json

interface AuthService {
    suspend fun signUp(name: String, email: String, password: String): AppResult<AuthSessionDto>
    suspend fun login(email: String, password: String): AppResult<AuthSessionDto>
    suspend fun deleteAccount(bearer: String): AppResult<Unit>
}

class AuthServiceImpl(
    private val client: SupabaseClient,
    private val json: Json,
) : AuthService {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): AppResult<AuthSessionDto> {
        val payload = json.encodeToString(
            SignUpRequestDto.serializer(),
            SignUpRequestDto(
                email = email,
                password = password,
                data = SignUpRequestDto.SignUpMetadata(name = name),
            ),
        )
        return client.authPost(
            path = "signup",
            body = payload,
        ) { body, _ -> json.decodeFromString(AuthSessionDto.serializer(), body) }
    }

    override suspend fun login(email: String, password: String): AppResult<AuthSessionDto> {
        val payload = json.encodeToString(
            LoginRequestDto.serializer(),
            LoginRequestDto(email = email, password = password),
        )
        return client.authPost(
            path = "token",
            query = mapOf("grant_type" to "password"),
            body = payload,
        ) { body, _ -> json.decodeFromString(AuthSessionDto.serializer(), body) }
    }

    override suspend fun deleteAccount(bearer: String): AppResult<Unit> =
        client.functionsPost(
            path = "delete-account",
            body = "{}",
            bearer = bearer,
        ) { _, _ -> }
}
