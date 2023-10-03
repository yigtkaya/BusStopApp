package com.example.voltlinescasestudy.ui.landing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voltlinescasestudy.data.location.DefaultLocationTracker
import com.example.voltlinescasestudy.data.repository.StationsRepositoryImpl
import com.example.voltlinescasestudy.util.Resource
import com.google.android.gms.maps.model.LatLng
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
    var userLocation: LatLng = LatLng(0.123, 5.245)

    init {
        fetchStations()
    }

    private fun fetchStations() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let {
                userLocation = LatLng(it.latitude, it.longitude)
            }
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
}