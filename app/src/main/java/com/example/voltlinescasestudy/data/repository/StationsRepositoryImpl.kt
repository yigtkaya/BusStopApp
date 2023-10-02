package com.example.voltlinescasestudy.data.repository

import com.example.voltlinescasestudy.data.remote.StationsApi
import com.example.voltlinescasestudy.domain.models.Station
import com.example.voltlinescasestudy.domain.repository.StationsRepository
import com.example.voltlinescasestudy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationsRepositoryImpl @Inject constructor(
    private val stationsApi: StationsApi
): StationsRepository {

    override suspend fun getStations(): Flow<Resource<List<Station>>> {
        return flow {
            emit(Resource.Loading(true))
            val stationListing = try {
                val response = stationsApi.getStations()

                if(response.isSuccessful) {
                    response.body()?.stations ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: IOException) {
                emit(Resource.Error(e.message.toString()))
                return@flow
            } catch (e: HttpException) {
                emit(Resource.Error(e.message.toString()))
                return@flow
            }

            stationListing.let {
                emit(Resource.Success(
                    data = stationListing
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun bookTrip() {
        TODO("Not yet implemented")
    }
}