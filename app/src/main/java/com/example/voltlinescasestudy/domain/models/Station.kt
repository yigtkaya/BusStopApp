package com.example.voltlinescasestudy.domain.models

import com.example.voltlinescasestudy.domain.models.Trip
import com.squareup.moshi.Json

data class Station(
    val id: Int,
    val name: String,
    @Json(name = "center_coordinates") val coordinates: String,
    @Json(name = "trips_count") val tripCount: Int,
    val trips: List<Trip>
)
