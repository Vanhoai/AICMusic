package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hinsun.domain.repositories.AuthRepository
import org.hinsun.domain.usecases.OAuthUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    
    @Provides
    @Singleton
    fun provideOAuthUseCase(authRepository: AuthRepository): OAuthUseCase {
        return OAuthUseCase(authRepository)
    }
}