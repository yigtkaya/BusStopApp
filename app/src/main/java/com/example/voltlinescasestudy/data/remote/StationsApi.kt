package com.example.voltlinescasestudy.data.remote

import com.example.voltlinescasestudy.data.local.StationsResponse
import com.example.voltlinescasestudy.domain.models.Trip
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface StationsApi {

    @GET("stations")
    suspend fun getStations() : Response<StationsResponse>

    @POST()
    suspend fun bookTrip(@Url url: String) : Response<Trip>

    companion object {
        const val BASE_URL = "https://demo.voltlines.com/case-study/6/"
    }
}