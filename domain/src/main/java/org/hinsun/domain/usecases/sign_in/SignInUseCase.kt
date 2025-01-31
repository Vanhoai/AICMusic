package org.hinsun.domain.usecases.sign_in

import arrow.core.getOrElse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.hinsun.core.https.Failure
import org.hinsun.core.https.HttpResponse
import org.hinsun.core.https.Response
import org.hinsun.domain.repositories.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(req: SignInRequest): Flow<HttpResponse<Response<SignInResponse>>> {
        return flow {
            emit(HttpResponse.HttpProcess())

            val response = authRepository.signIn(req).getOrElse { failure ->
                emit(HttpResponse.HttpFailure(failure))
                return@flow
            }

            if (response.isFailure()) {
                emit(
                    HttpResponse.HttpFailure(
                        Failure.RequestFailure(
                            message = response.message,
                            status = response.status
                        )
                    )
                )
                return@flow
            }

            emit(HttpResponse.HttpSuccess(response))
        }
    }
}