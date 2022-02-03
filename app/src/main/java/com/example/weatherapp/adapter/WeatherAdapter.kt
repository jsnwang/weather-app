package com.example.weatherapp.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.HourlyForecastBinding
import com.example.weatherapp.model.response.Hourly
import com.example.weatherapp.model.response.Weather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

class WeatherAdapter(private val hourlyWeather : List<Hourly>, private val weather: Weather) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = WeatherViewHolder.newInstance(parent)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindWeather(hourlyWeather[position], weather)
    }

    override fun getItemCount() = hourlyWeather.size
    class WeatherViewHolder(private val binding : HourlyForecastBinding)
        : RecyclerView.ViewHolder(binding.root) {
            @RequiresApi(Build.VERSION_CODES.O)
            fun bindWeather(hourly : Hourly, weather: Weather) {
                val zoneId = ZoneId.of(weather.timezone)
                val formatter = DateTimeFormatter.ofPattern("ha", Locale.ENGLISH)
                val instant = Instant.ofEpochSecond(hourly.dt.toLong())
                val time = instant.atZone(zoneId).format(formatter)
                Log.d("Time:", time.toString())

                val pre = hourly.pop*100
                binding.tvHourlyTime.text = time.toString()
                binding.tvHourlyTemp.text = hourly.temp.roundToInt().toString() + "\u00B0"
                binding.tvHourlyRain.text = pre.roundToInt().toString() + "%"
                binding.tvHourlyWind.text = hourly.windSpeed.toString()
                binding.tvHourlyHumid.text = hourly.humidity.toString() + "%"

                when(hourly.weather[0].main){
                    "Clouds" -> binding.ivHourlyIcon.setImageResource(R.drawable.ic_cloudy)
                    "Clear" -> binding.ivHourlyIcon.setImageResource(R.drawable.ic_sunny)
                    "Thunderstorm" -> binding.ivHourlyIcon.setImageResource(R.drawable.ic_heavy_rain)
                    "Rain" -> binding.ivHourlyIcon.setImageResource(R.drawable.ic_rainfall)
                    "Drizzle" -> binding.ivHourlyIcon.setImageResource(R.drawable.ic_light_rain)
                    "Snow" -> binding.ivHourlyIcon.setImageResource(R.drawable.ic_snow_sleet)
                    "Atmosphere" -> binding.ivHourlyIcon.setImageResource(R.drawable.ic_partly_cloudy)
                }
            }

        companion object{
            fun newInstance(parent: ViewGroup) = HourlyForecastBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ).let { WeatherViewHolder(it) } }


        }
}