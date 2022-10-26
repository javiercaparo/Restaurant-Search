package com.michaelmccormick.restaurantsearch.network.di

import com.michaelmccormick.restaurantsearch.network.services.JustEatApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Singleton
    @Provides
    fun provideJustEatApiService(): JustEatApiService {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://uk.api.just-eat.io/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}
