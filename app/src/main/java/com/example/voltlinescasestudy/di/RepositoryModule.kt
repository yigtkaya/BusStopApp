package com.example.voltlinescasestudy.di

import com.example.voltlinescasestudy.data.repository.StationsRepositoryImpl
import com.example.voltlinescasestudy.domain.repository.StationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStationsRepository(
        stationsRepositoryImpl: StationsRepositoryImpl
    ) : StationsRepository

}