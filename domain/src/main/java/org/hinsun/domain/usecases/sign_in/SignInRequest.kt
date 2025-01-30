package org.hinsun.domain.usecases.sign_in

data class SignInRequest(
    val email: String,
    val idToken: String,
    val deviceToken: String,
)