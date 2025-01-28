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

    companion object {
        private const val PREFIX = "@org.hinsun.storage"
        const val ACCESS_TOKEN = "$PREFIX.accessToken"
        const val REFRESH_TOKEN = "$PREFIX.refreshToken"
    }
}