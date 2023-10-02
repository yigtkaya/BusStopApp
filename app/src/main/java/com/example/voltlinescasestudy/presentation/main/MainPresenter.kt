package com.example.voltlinescasestudy.presentation.main

import com.example.voltlinescasestudy.data.repository.StationsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainPresenter @Inject constructor(
    private val view: MainView,
    private val stationsRepositoryImpl: StationsRepositoryImpl
) {

    suspend fun fetchStations() {

    }
}