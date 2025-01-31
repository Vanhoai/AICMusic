package org.hinsun.domain.usecases.sign_in

data class SignInRequest(
    val uuid: String,
    val email: String,
    val name: String,
    val avatar: String,
    val deviceToken: String,
    val idToken: String,
)