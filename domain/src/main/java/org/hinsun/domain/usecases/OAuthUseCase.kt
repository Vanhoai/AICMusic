package org.hinsun.domain.usecases

import kotlinx.coroutines.flow.Flow
import org.hinsun.core.https.HttpResponse
import org.hinsun.core.https.Response
import org.hinsun.domain.models.OAuthRequest
import org.hinsun.domain.models.OAuthResponse
import org.hinsun.domain.repositories.AuthRepository
import javax.inject.Inject

class OAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(req: OAuthRequest): Flow<HttpResponse<Response<OAuthResponse>>> {
        return authRepository.oAuth(req)
    }
}