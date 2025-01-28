package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.hinsun.core.storage.AppStorage
import org.hinsun.domain.repositories.AuthRepository
import org.hinsun.domain.usecases.OAuthUseCase

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun provideOAuthUseCase(authRepository: AuthRepository, appStorage: AppStorage): OAuthUseCase {
        return OAuthUseCase(authRepository, appStorage)
    }
}