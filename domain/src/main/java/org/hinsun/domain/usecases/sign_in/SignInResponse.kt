package org.hinsun.domain.usecases.sign_in

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
)