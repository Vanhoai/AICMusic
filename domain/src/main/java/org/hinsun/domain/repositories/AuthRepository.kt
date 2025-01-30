package org.hinsun.domain.repositories

import arrow.core.Either
import org.hinsun.core.https.Failure
import org.hinsun.core.https.Response
import org.hinsun.domain.usecases.sign_in.SignInRequest
import org.hinsun.domain.usecases.sign_in.SignInResponse
import org.hinsun.domain.usecases.verify.VerifyIdTokenRequest
import org.hinsun.domain.usecases.verify.VerifyIdTokenResponse

interface AuthRepository {
    suspend fun verifyIdToken(req: VerifyIdTokenRequest): Either<Failure, Response<VerifyIdTokenResponse>>
    suspend fun signIn(req: SignInRequest): Either<Failure, Response<SignInResponse>>
}