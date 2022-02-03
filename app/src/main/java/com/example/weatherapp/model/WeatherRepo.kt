package com.example.weatherapp.model

import android.util.Log
import com.example.weatherapp.model.remote.RetrofitInstance
import com.example.weatherapp.model.remote.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object WeatherRepo {

    private val weatherService by lazy {RetrofitInstance().weatherService}

    private var units = "imperial"
    suspend fun getWeather(lat: Double, long: Double) = withContext(Dispatchers.IO){
        val response = weatherService.getWeather(lat, long, units, WeatherService.APP_ID)
        Log.d("response", response.toString())
        response.body()

    }

    suspend fun getLocation(city: String) = withContext(Dispatchers.IO){
        val response = weatherService.getLocation(city, WeatherService.APP_ID)
        response.body()

    }
}