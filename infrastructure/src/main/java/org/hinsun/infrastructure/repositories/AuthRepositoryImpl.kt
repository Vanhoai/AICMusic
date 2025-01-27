package org.hinsun.infrastructure.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.hinsun.core.https.HttpResponse
import org.hinsun.core.https.Response
import org.hinsun.domain.models.OAuthRequest
import org.hinsun.domain.models.OAuthResponse
import org.hinsun.domain.repositories.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun oAuth(req: OAuthRequest): Flow<HttpResponse<Response<OAuthResponse>>> {
        return flow {}
    }
}