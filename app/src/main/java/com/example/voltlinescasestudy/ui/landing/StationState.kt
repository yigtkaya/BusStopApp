package com.example.voltlinescasestudy.ui.landing

import com.example.voltlinescasestudy.domain.models.Station

data class StationState(
    val stations: List<Station> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
