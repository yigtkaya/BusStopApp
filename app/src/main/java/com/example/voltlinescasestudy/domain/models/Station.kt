package com.example.voltlinescasestudy.domain.models

import com.example.voltlinescasestudy.domain.models.Trip

data class Station(
    val id: Int,
    val name: String,
    val coordinates: String,
    val tripCount: Int,
    val trips: List<Trip>
)
