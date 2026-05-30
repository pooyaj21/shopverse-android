package com.shopverse.core.service.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("data") val data: SignUpMetadata,
) {
    @Serializable
    data class SignUpMetadata(
        @SerialName("name") val name: String,
    )
}

@Serializable
data class LoginRequestDto(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)

@Serializable
data class AuthSessionDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("expires_in") val expiresIn: Long? = null,
    @SerialName("user") val user: AuthUserDto? = null,
)

@Serializable
data class AuthUserDto(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String? = null,
    @SerialName("user_metadata") val metadata: UserMetadataDto? = null,
) {
    @Serializable
    data class UserMetadataDto(
        @SerialName("name") val name: String? = null,
    )
}
