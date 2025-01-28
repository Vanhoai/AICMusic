package org.hinsun.infrastructure.repositories

import arrow.core.Either
import org.hinsun.core.https.Failure
import org.hinsun.core.https.Response
import org.hinsun.domain.models.OAuthRequest
import org.hinsun.domain.models.OAuthResponse
import org.hinsun.domain.repositories.AuthRepository
import org.hinsun.infrastructure.datasources.local.AccountLocalDatasource
import org.hinsun.infrastructure.datasources.remote.AuthRemoteDatasource
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDatasource: AuthRemoteDatasource,
    private val accountLocalDatasource: AccountLocalDatasource
) : AuthRepository {

    override suspend fun oAuth(req: OAuthRequest): Either<Failure, Response<OAuthResponse>> {
        try {
            val posts = authRemoteDatasource.getPosts()
            Timber.tag("HinsunMusic").d("Posts: $posts")

            val response = authRemoteDatasource.oauth()
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