package org.hinsun.music.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hinsun.infrastructure.datasources.remote.AuthRemoteDatasource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val BASE_URL = "http://192.168.1.8:8080/api/v1/"
        private const val AUTH_SUFFIX = "auth/"
    }

    private val interceptor by lazy { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }
    private val okHttpClient by lazy { OkHttpClient.Builder().addInterceptor(interceptor).build() }

    @Provides
    @Singleton
    fun providerAuthRemoteDatasource(): AuthRemoteDatasource {
        return Retrofit.Builder()
            .baseUrl(BASE_URL + AUTH_SUFFIX)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthRemoteDatasource::class.java)
    }
}