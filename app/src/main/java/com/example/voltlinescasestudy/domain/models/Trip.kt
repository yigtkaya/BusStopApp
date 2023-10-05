package com.example.voltlinescasestudy.domain.models

import com.squareup.moshi.Json

data class Trip(
    val id: Int,
    @Json(name = "bus_name") val bus_name: String,
    val time: String
)
