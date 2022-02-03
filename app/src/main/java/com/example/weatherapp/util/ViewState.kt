package com.example.weatherapp.util

import com.example.weatherapp.model.response.City
import com.example.weatherapp.model.response.Weather
import retrofit2.Response

sealed class ViewState {

    object Loading : ViewState()

    data class SuccessWeather(val weather: Weather) : ViewState()

    data class SuccessCity(val city: List<City>) : ViewState()

    data class Error(val error: String) : ViewState()
}