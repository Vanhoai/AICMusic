package org.hinsun.domain.repositories

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import org.hinsun.core.https.HttpResponse
import org.hinsun.core.https.Response
import org.hinsun.domain.models.OAuthRequest
import org.hinsun.domain.models.OAuthResponse

interface AuthRepository {
    suspend fun oAuth(req: OAuthRequest): Flow<HttpResponse<Response<OAuthResponse>>>
}