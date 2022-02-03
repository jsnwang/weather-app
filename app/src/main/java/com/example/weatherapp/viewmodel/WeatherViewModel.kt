package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherRepo
import com.example.weatherapp.util.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel : ViewModel() {
    private val _state = MutableStateFlow<ViewState>(ViewState.Loading)
    val state: StateFlow<ViewState> get() = _state

    fun weatherSearch(lat: Double = 51.5, lon: Double = -0.12){
        viewModelScope.launch {
            val state = try{
                val weather = WeatherRepo.getWeather(lat, lon)!!
                ViewState.SuccessWeather(weather)
            } catch(ex: Exception) {
                ViewState.Error(ex.message?:"Catch Error")
            }

            _state.value = state
        }
    }

    fun citySearch(city: String = "Chelsea"){
        viewModelScope.launch {
            val state = try{
                val cities = WeatherRepo.getLocation(city)!!
                    Log.d("cities", cities[0].toString())
                    ViewState.SuccessCity(cities)
            } catch(ex: Exception) {
                ViewState.Error(ex.message?:"Cities endpoint error")
            }

            _state.value = state

        }
    }
}