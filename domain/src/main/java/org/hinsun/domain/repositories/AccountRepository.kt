package org.hinsun.domain.repositories

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import org.hinsun.core.https.Failure
import org.hinsun.core.https.Response
import org.hinsun.domain.entities.AccountEntity

interface AccountRepository {
    suspend fun findAccountByEmail(): Flow<Either<Failure, Response<AccountEntity>>>
}