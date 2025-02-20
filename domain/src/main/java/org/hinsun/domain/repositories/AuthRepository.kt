package org.hinsun.domain.repositories

import arrow.core.Either
import org.hinsun.core.https.Failure
import org.hinsun.core.https.Response
import org.hinsun.domain.usecases.sign_in.SignInRequest
import org.hinsun.domain.usecases.sign_in.SignInResponse

interface AuthRepository {
    suspend fun signIn(req: SignInRequest): Either<Failure, Response<SignInResponse>>
}