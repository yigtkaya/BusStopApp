package com.example.voltlinescasestudy.data.repository

import com.example.voltlinescasestudy.domain.models.Station
import com.example.voltlinescasestudy.domain.repository.StationsRepository
import com.example.voltlinescasestudy.util.Resource
import kotlinx.coroutines.flow.Flow

class StationsRepositoryImpl : StationsRepository {
    override suspend fun getStations(): Flow<Resource<List<Station>>> {
        TODO("Not yet implemented")
    }

    override suspend fun bookTrip() {
        TODO("Not yet implemented")
    }


}