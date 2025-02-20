package org.hinsun.infrastructure.datasources.remote

import org.hinsun.core.https.Response
import org.hinsun.domain.usecases.sign_in.SignInRequest
import org.hinsun.domain.usecases.sign_in.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRemoteDatasource {
    companion object {
        private const val AUTH_SUFFIX = "/api/v1/auth"
    }

    @POST("${AUTH_SUFFIX}/sign-in")
    suspend fun signIn(@Body req: SignInRequest): Response<SignInResponse>
}