package org.hinsun.music.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hinsun.core.storage.AppStorage
import org.hinsun.core.storage.CryptoStorage
import org.hinsun.core.storage.CryptoStorageImpl
import org.hinsun.core.storage.HinsunStorage
import org.hinsun.core.storage.HinsunStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {

    @Provides
    @Singleton
    fun provideHinsunStorage(application: Application): HinsunStorage {
        return HinsunStorageImpl(application)
    }

    @Provides
    @Singleton
    fun provideAppStorage(hinsunStorage: HinsunStorage): AppStorage {
        return AppStorage(hinsunStorage)
    }

    @Provides
    @Singleton
    fun provideCryptoStorage(hinsunStorage: HinsunStorage): CryptoStorage {
        return CryptoStorageImpl(hinsunStorage)
    }
}