package org.hinsun.music.domain.usecases.sign_in

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
)