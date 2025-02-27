package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hinsun.music.domain.repositories.AudioRepository
import org.hinsun.music.domain.repositories.AuthRepository
import org.hinsun.music.infrastructure.datasources.local.AccountLocalDatasource
import org.hinsun.music.infrastructure.datasources.remote.AuthRemoteDatasource
import org.hinsun.music.infrastructure.repositories.AudioRepositoryImpl
import org.hinsun.music.infrastructure.repositories.AuthRepositoryImpl
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

    @Provides
    @Singleton
    fun provideAudioRepository(): AudioRepository {
        return AudioRepositoryImpl()
    }
}