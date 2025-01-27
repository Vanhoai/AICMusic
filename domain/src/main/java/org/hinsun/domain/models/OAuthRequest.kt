package org.hinsun.domain.models

data class OAuthRequest(
    val idToken: String,
    val deviceToken: String
)