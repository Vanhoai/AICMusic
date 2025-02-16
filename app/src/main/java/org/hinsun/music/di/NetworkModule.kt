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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        private const val BASE_URL = "http://192.168.1.21:8080/"
    }

    private val interceptor by lazy { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providerRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providerAuthRemoteDatasource(retrofit: Retrofit): AuthRemoteDatasource {
        return retrofit.create(AuthRemoteDatasource::class.java)
    }
}