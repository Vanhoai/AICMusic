package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hinsun.music.infrastructure.datasources.local.AccountLocalDatasource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {
    @Provides
    @Singleton
    fun provideAccountLocalDatasource(): AccountLocalDatasource {
        return AccountLocalDatasource()
    }
}