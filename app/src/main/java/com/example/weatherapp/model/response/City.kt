package com.example.weatherapp.model.response


import com.google.gson.annotations.SerializedName

data class City(
    val country: String,
    val lat: Double,
    @SerializedName("local_names")
    val localNames: LocalNames,
    val lon: Double,
    val name: String,
    val state: String
)