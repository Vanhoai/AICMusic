package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hinsun.domain.repositories.AuthRepository
import org.hinsun.infrastructure.datasources.local.AccountLocalDatasource
import org.hinsun.infrastructure.datasources.remote.AuthRemoteDatasource
import org.hinsun.infrastructure.repositories.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authRemoteDatasource: AuthRemoteDatasource,
        accountLocalDatasource: AccountLocalDatasource
    ): AuthRepository {
        return AuthRepositoryImpl(authRemoteDatasource, accountLocalDatasource)
    }
}