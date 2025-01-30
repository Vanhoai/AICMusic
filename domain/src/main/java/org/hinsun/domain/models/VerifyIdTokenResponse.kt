package org.hinsun.domain.models

data class VerifyIdTokenResponse(
    val authTime: Long,
    val exp: Long,
    val iat: Long,
    val iss: String,
    val sub: String,
)