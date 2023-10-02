package com.example.voltlinescasestudy.data.local

import com.example.voltlinescasestudy.domain.models.Station
import com.squareup.moshi.Json

data class StationsResponse(
    @field:Json(name = "stations")
    val articles: List<Station>,
)
