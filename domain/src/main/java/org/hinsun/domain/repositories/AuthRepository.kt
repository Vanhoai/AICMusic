package org.hinsun.domain.repositories

import arrow.core.Either
import org.hinsun.core.https.Failure
import org.hinsun.core.https.Response
import org.hinsun.domain.models.VerifyIdTokenRequest
import org.hinsun.domain.models.VerifyIdTokenResponse

interface AuthRepository {
    suspend fun verifyIdToken(req: VerifyIdTokenRequest): Either<Failure, Response<VerifyIdTokenResponse>>
}