package org.hinsun.domain.repositories

import arrow.core.Either
import org.hinsun.core.https.Failure
import org.hinsun.core.https.Response
import org.hinsun.domain.models.OAuthRequest
import org.hinsun.domain.models.OAuthResponse

interface AuthRepository {
    suspend fun oAuth(req: OAuthRequest): Either<Failure, Response<OAuthResponse>>
}