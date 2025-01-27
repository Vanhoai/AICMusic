package org.hinsun.domain.models

data class OAuthResponse(
    val accessToken: String,
    val refreshToken: String,
)