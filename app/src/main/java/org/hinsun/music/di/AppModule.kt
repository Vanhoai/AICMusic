package org.hinsun.music.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        RepositoryModule::class,
        UseCaseModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
class AppModule {}