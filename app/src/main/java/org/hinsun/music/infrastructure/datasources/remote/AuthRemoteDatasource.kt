package org.hinsun.music.infrastructure.datasources.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.hinsun.core.https.Response
import org.hinsun.music.domain.usecases.sign_in.SignInRequest
import org.hinsun.music.domain.usecases.sign_in.SignInResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDatasource @Inject constructor(
    private val httpClient: HttpClient
) {
    companion object {
        private const val AUTH_SUFFIX = "api/v1/auth"
    }

    suspend fun signIn(req: org.hinsun.music.domain.usecases.sign_in.SignInRequest): Response<org.hinsun.music.domain.usecases.sign_in.SignInResponse> {
        val response = httpClient.post("$AUTH_SUFFIX/sign-in") {
            setBody(req)
        }

        return response.body()
    }
}