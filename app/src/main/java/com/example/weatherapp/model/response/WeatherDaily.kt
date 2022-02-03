package com.example.weatherapp.model.response


import com.google.gson.annotations.SerializedName

data class WeatherDaily(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)