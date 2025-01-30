package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.hinsun.domain.repositories.AuthRepository
import org.hinsun.domain.usecases.sign_in.SignInUseCase
import org.hinsun.domain.usecases.verify.VerifyIdTokenUseCase

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun provideVerifyIdTokenUseCase(authRepository: AuthRepository): VerifyIdTokenUseCase {
        return VerifyIdTokenUseCase(authRepository)
    }

    @Provides
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase {
        return SignInUseCase(authRepository)
    }
}