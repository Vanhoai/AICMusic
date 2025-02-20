package org.hinsun.core.storage

import javax.inject.Inject

class AppStorage @Inject constructor(
    private val hinsunStorage: HinsunStorage
) {
    fun writeAuthSession(
        accessToken: String,
        refreshToken: String,
    ): Boolean {
        return hinsunStorage.write(ACCESS_TOKEN, accessToken) &&
                hinsunStorage.write(REFRESH_TOKEN, refreshToken)
    }

    fun readAccessToken(): String {
        return hinsunStorage.read(ACCESS_TOKEN, defaultValue = "")
    }

    fun readRefreshToken(): String {
        return hinsunStorage.read(REFRESH_TOKEN, defaultValue = "")
    }

    fun readIsEnableBiometric(): Boolean {
        return hinsunStorage.read(
            IS_ENABLE_BIOMETRIC,
            defaultValue = false
        )
    }

    fun readIsEnableCryptoStorage(): Boolean {
        return hinsunStorage.read(
            IS_ENABLE_CRYPTO_STORAGE,
            defaultValue = false
        )
    }

    companion object {
        private const val PREFIX = "@org.hinsun.storage"

        const val ACCESS_TOKEN = "$PREFIX.accessToken"
        const val REFRESH_TOKEN = "$PREFIX.refreshToken"
        const val IS_ENABLE_BIOMETRIC = "$PREFIX.isEnableBiometric"
        const val IS_ENABLE_CRYPTO_STORAGE = "$PREFIX.isEnableCryptoStorage"
    }
}