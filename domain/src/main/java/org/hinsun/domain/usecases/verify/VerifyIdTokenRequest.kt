package org.hinsun.domain.usecases.verify

data class VerifyIdTokenRequest(
    val idToken: String,
    val uuid: String,
)