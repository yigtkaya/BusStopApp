package com.example.voltlinescasestudy.di

import com.example.voltlinescasestudy.data.remote.StationsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStationsApi() : StationsApi {
        return Retrofit.Builder()
            .baseUrl(StationsApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StationsApi::class.java)
    }
}