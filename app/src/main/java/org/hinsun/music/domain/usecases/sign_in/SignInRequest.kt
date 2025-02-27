package org.hinsun.music.domain.usecases.sign_in

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val uuid: String,
    val email: String,
    val name: String,
    val avatar: String,
    val deviceToken: String,
    val idToken: String,
)