package com.example.voltlinescasestudy.data.remote

import com.example.voltlinescasestudy.data.local.StationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface StationsApi {

    @GET("https://demo.voltlines.com/case-study/6/stations/")
    suspend fun getStations(

    ) : Response<StationsResponse>

    @POST("https://demo.voltlines.com/case-study/6/stations/<station_id>/trips/<trip_id>")
    suspend fun bookTrip(

    )
}