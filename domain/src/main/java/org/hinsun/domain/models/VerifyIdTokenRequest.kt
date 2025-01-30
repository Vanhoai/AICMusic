package org.hinsun.domain.models

data class VerifyIdTokenRequest(
    val idToken: String,
    val uuid: String,
)