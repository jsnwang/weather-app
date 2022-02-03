package com.example.weatherapp.model.remote

import com.example.weatherapp.model.response.City
import com.example.weatherapp.model.response.Weather
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    companion object {
        const val BASE_URL = "http://api.openweathermap.org/"
        const val APP_ID = "e983b7a78cf877d64aabcb011d94f3a8"
    }

    @GET("data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String,
        @Query("appid") key: String
    ) : Response<Weather>

    @GET("geo/1.0/direct")
    suspend fun getLocation(
        @Query("q") city: String,
        @Query("appid") key: String
    ) : Response<List<City>>
}