package com.example.voltlinescasestudy.domain.models

import com.squareup.moshi.Json

data class Station(
    val id: Int,
    val name: String,
    @Json(name = "center_coordinates") val center_coordinates: String,
    @Json(name = "trips_count") val trips_count: Int,
    val trips: List<Trip>
)
