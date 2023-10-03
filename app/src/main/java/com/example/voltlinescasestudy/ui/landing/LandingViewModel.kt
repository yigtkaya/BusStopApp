package com.example.voltlinescasestudy.ui.landing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voltlinescasestudy.data.repository.StationsRepositoryImpl
import com.example.voltlinescasestudy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val repository: StationsRepositoryImpl
) : ViewModel() {

    var state by mutableStateOf(StationState())
    init {
        fetchStations()
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
}