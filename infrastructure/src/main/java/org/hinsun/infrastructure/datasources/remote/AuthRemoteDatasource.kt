package org.hinsun.infrastructure.datasources.remote

import arrow.core.Either
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.hinsun.core.https.Response
import org.hinsun.domain.usecases.sign_in.SignInRequest
import org.hinsun.domain.usecases.sign_in.SignInResponse

class AuthRemoteDatasource(
    private val httpClient: HttpClient
) {
    companion object {
        private const val AUTH_SUFFIX = "/api/v1/auth"
    }

    suspend fun signIn(req: SignInRequest): Response<SignInResponse> {
        val response = httpClient.post("$AUTH_SUFFIX/sign-in") {
            setBody(req)
        }

        return response.body()
    }
}