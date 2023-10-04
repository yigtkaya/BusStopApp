package com.example.voltlinescasestudy.ui.landing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voltlinescasestudy.data.location.DefaultLocationTracker
import com.example.voltlinescasestudy.data.repository.StationsRepositoryImpl
import com.example.voltlinescasestudy.util.Resource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class LandingViewModel @Inject constructor(
    private val repository: StationsRepositoryImpl,
    private val locationTracker: DefaultLocationTracker
    ) : ViewModel() {

    var state by mutableStateOf(StationState())
    var mapState by mutableStateOf(MapState())
    var cameraPositionState by mutableStateOf(CameraPositionState()) // Initialize it as null

    init {
        fetchCurrentLocation()
        fetchStations()
    }

    private fun fetchCurrentLocation() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let {
                mapState = mapState.copy(
                    lastKnownLocation = it
                )
                cameraPositionState = CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(LatLng(it.latitude, it.longitude), 15f)
                )
            }
        }
    }

    private fun fetchStations() {
            viewModelScope.launch {
            repository.getStations()
                .collect { result ->
                    when(result) {
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                        is Resource.Success -> {
                            result.data?.let {
                                state = state.copy(stations = it)
                            }
                        }
                    }
                }
        }
    }
    fun calculateInitialCameraPosition(userLocation: LatLng): CameraPosition {
        return CameraPosition.fromLatLngZoom(userLocation, 10f)
    }
}