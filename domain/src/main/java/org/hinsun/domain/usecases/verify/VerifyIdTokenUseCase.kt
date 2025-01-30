package org.hinsun.domain.usecases.verify

import arrow.core.getOrElse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.hinsun.core.https.HttpResponse
import org.hinsun.core.https.Response
import org.hinsun.domain.repositories.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class VerifyIdTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(req: VerifyIdTokenRequest): Flow<HttpResponse<Response<VerifyIdTokenResponse>>> {
        return flow {
            emit(HttpResponse.HttpProcess())

            val response = authRepository.verifyIdToken(req).getOrElse { failure ->
                emit(HttpResponse.HttpFailure(failure))
                return@flow
            }

            Timber.tag("HinsunMusic").d("VerifyIdTokenUseCase: $response")
            emit(HttpResponse.HttpSuccess(response))
        }
    }
}