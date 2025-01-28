package org.hinsun.domain.usecases

import arrow.core.getOrElse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.hinsun.core.https.Failure
import org.hinsun.core.https.HttpResponse
import org.hinsun.core.https.Response
import org.hinsun.core.storage.AppStorage
import org.hinsun.domain.models.OAuthRequest
import org.hinsun.domain.models.OAuthResponse
import org.hinsun.domain.repositories.AuthRepository
import timber.log.Timber
import javax.inject.Inject

class OAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val appStorage: AppStorage
) {
    operator fun invoke(req: OAuthRequest): Flow<HttpResponse<Response<OAuthResponse>>> {
        return flow {

            Timber.tag("HinsunMusic").d("Request: $req")
            emit(HttpResponse.HttpProcess())

            val response = authRepository.oAuth(req).getOrElse { failure ->
                Timber.tag("HinsunMusic").d("Failure: $failure")
                emit(HttpResponse.HttpFailure(failure))
                return@flow
            }

            Timber.tag("HinsunMusic").d("Success: $response")
            appStorage.writeAuthSession(
                accessToken = response.data!!.accessToken,
                refreshToken = response.data!!.refreshToken
            )

            emit(HttpResponse.HttpSuccess(response))
        }
    }
}