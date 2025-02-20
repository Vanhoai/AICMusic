package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import okhttp3.OkHttpClient
import org.hinsun.infrastructure.datasources.remote.AuthRemoteDatasource
import org.hinsun.music.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient {
            defaultRequest {
                headers {}
                url(BuildConfig.BASE_URL)
            }
        }
    }

    @Provides
    @Singleton
    fun providerAuthRemoteDatasource(httpClient: HttpClient): AuthRemoteDatasource {
        return AuthRemoteDatasource(httpClient)
    }
}