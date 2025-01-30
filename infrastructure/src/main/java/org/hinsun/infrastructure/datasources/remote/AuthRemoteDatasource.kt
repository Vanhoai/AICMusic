package org.hinsun.infrastructure.datasources.remote

import org.hinsun.core.https.Response
import org.hinsun.domain.models.VerifyIdTokenRequest
import org.hinsun.domain.models.VerifyIdTokenResponse
import retrofit2.http.POST

interface AuthRemoteDatasource {
    @POST("/verify-token")
    suspend fun verifyIdToken(req: VerifyIdTokenRequest): Response<VerifyIdTokenResponse>
}