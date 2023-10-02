package com.example.voltlinescasestudy.domain.repository

import com.example.voltlinescasestudy.domain.models.Station
import com.example.voltlinescasestudy.util.Resource
import kotlinx.coroutines.flow.Flow

interface StationsRepository {

    suspend fun getStations() : Flow<Resource<List<Station>>>

    suspend fun bookTrip()
}