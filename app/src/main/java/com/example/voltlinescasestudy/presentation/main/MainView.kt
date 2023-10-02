package com.example.voltlinescasestudy.presentation.main

interface MainView {

    fun showLoading()
    fun showStations()
    fun showError(errorMessage: String)

}