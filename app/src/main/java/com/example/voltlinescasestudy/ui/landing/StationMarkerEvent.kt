package com.example.voltlinescasestudy.ui.landing

sealed class StationMarkerEvent {
    object Refresh: StationMarkerEvent()
    object onClick: StationMarkerEvent()
}