package org.ic.tech.domain.repositories

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import org.ic.tech.core.models.Failure
import org.ic.tech.core.models.Response
import org.ic.tech.domain.entities.AccountEntity

interface AccountRepository {
    suspend fun findAccountByEmail(): Flow<Either<Failure, Response<AccountEntity>>>
}