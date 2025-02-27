package org.hinsun.music.infrastructure.repositories

import arrow.core.Either
import org.hinsun.core.https.Failure
import org.hinsun.core.https.Response
import org.hinsun.music.domain.repositories.AuthRepository
import org.hinsun.music.domain.usecases.sign_in.SignInRequest
import org.hinsun.music.domain.usecases.sign_in.SignInResponse
import org.hinsun.music.infrastructure.datasources.local.AccountLocalDatasource
import org.hinsun.music.infrastructure.datasources.remote.AuthRemoteDatasource
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDatasource: AuthRemoteDatasource,
    private val accountLocalDatasource: AccountLocalDatasource
) : org.hinsun.music.domain.repositories.AuthRepository {

    override suspend fun signIn(req: org.hinsun.music.domain.usecases.sign_in.SignInRequest): Either<Failure, Response<org.hinsun.music.domain.usecases.sign_in.SignInResponse>> {
        return runWithCatching {
            val response = authRemoteDatasource.signIn(req)
            return@runWithCatching response
        }
    }

    private suspend fun <R> runWithCatching(block: suspend () -> R): Either<Failure, R> {
        try {
            val response = block()
            return Either.Right(response)
        } catch (ioException: IOException) {
            return Either.Left(
                Failure.IOFailure(
                    status = 500,
                    message = ioException.message,
                    cause = ioException
                )
            )
        } catch (exception: Exception) {
            return Either.Left(
                Failure.ExceptionFailure(
                    status = 500,
                    message = exception.message,
                    cause = exception
                )
            )
        }
    }
}