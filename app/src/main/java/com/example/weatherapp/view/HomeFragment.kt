package com.example.weatherapp.view

import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.model.response.City
import com.example.weatherapp.model.response.Weather
import com.example.weatherapp.util.ViewState
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.collect
import com.example.weatherapp.R
import com.example.weatherapp.adapter.WeatherAdapter
import java.sql.Time
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.roundToInt
import androidx.appcompat.widget.SearchView

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WeatherViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    private fun initViews() = with(binding){
        middleData.mon.day.text = getString(R.string._mon)
        middleData.tue.day.text = getString(R.string._tue)
        middleData.wed.day.text = getString(R.string._wed)
        middleData.thu.day.text = getString(R.string._thu)
        middleData.fri.day.text = getString(R.string._fri)
        middleData.sat.day.text = getString(R.string._sat)
        middleData.sun.day.text = getString(R.string._sun)

        viewModel.weatherSearch()

        searchButton.setOnClickListener {
            searchScreenLayout.searchScreen.isVisible = true
        }

        searchScreenLayout.exitSearchButton.setOnClickListener {
            searchScreenLayout.searchScreen.isVisible = false
        }

        searchScreenLayout.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.citySearch(query!!)
                searchScreenLayout.searchScreen.isVisible = false
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObservers() = with(viewModel){
        lifecycleScope.launchWhenStarted{
            state.collect { state ->
                binding.loader.isVisible = state is ViewState.Loading
                Log.d("state", state.toString())
                if (state is ViewState.SuccessWeather) handleSuccessWeather(state.weather)
                if (state is ViewState.SuccessCity) handleSuccessCity(state.city)
                if (state is ViewState.Error) handleError(state.error)
            }
        }
    }

    private fun handleError(exception: String) {
        Toast.makeText(context, exception, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSuccessWeather(weather: Weather) = with(binding){
        topData.tvTemp.text = getString(R.string.tempDisplay, weather.current.temp.roundToInt().toString())
        val timeZone = ZoneId.of(weather.timezone.toString())
        Log.d("timeZone", timeZone.toString())
        Log.d("timezone", weather.timezone)
        val currentTime = ZonedDateTime.now(timeZone)
        Log.d("currentTime", currentTime.toString())

        val formatter = DateTimeFormatter.ofPattern("EEE M/dd/yy   h:mm a")
        val formattedTime = currentTime.format(formatter)
        topData.tvDate.text = formattedTime


        binding.bottomData.rvForecast.adapter = WeatherAdapter(weather.hourly, weather)
        setDailyTemperature(weather)
        setDailyIcons(weather)
    }
    private fun handleSuccessCity(city: List<City>) {
        if(city[0].state.isNotEmpty())
            binding.topData.tvCity.text = getString(R.string.cityDisplay, city[0].name, city[0].state)
        else
            binding.topData.tvCity.text = getString(R.string.cityDisplay, city[0].name, city[0].country)
        viewModel.weatherSearch(city[0].lat, city[0].lon)
        Log.d("Success", city[0].name)
    }

    private fun setDailyTemperature(weather: Weather) = with(binding)
    {
        middleData.mon.tvDailyTemp.text = getString(R.string.tempDisplay,
            weather.daily[0].temp.day.roundToInt().toString())
        middleData.tue.tvDailyTemp.text = getString(R.string.tempDisplay,
            weather.daily[1].temp.day.roundToInt().toString())
        middleData.wed.tvDailyTemp.text = getString(R.string.tempDisplay,
            weather.daily[2].temp.day.roundToInt().toString())
        middleData.thu.tvDailyTemp.text = getString(R.string.tempDisplay,
            weather.daily[3].temp.day.roundToInt().toString())
        middleData.fri.tvDailyTemp.text = getString(R.string.tempDisplay,
            weather.daily[4].temp.day.roundToInt().toString())
        middleData.sat.tvDailyTemp.text = getString(R.string.tempDisplay,
            weather.daily[5].temp.day.roundToInt().toString())
        middleData.sun.tvDailyTemp.text = getString(R.string.tempDisplay,
            weather.daily[6].temp.day.roundToInt().toString())
    }

    private fun setDailyIcons(weather: Weather) = with(binding){

        for(i in 0 until weather.daily.count()) {
            var dateImageView = middleData.mon.ivWeather
            when(i)
            {
                0 -> dateImageView = middleData.mon.ivWeather
                1 -> dateImageView = middleData.tue.ivWeather
                2 -> dateImageView = middleData.wed.ivWeather
                3 -> dateImageView = middleData.thu.ivWeather
                4 -> dateImageView = middleData.fri.ivWeather
                5 -> dateImageView = middleData.sat.ivWeather
                6 -> dateImageView = middleData.sun.ivWeather
            }
            when (weather.daily[i].weather[0].main) {
                "Clouds" -> dateImageView.setImageResource(R.drawable.ic_cloudy)
                "Clear" -> dateImageView.setImageResource(R.drawable.ic_sunny)
                "Thunderstorm" -> dateImageView.setImageResource(R.drawable.ic_heavy_rain)
                "Rain" -> dateImageView.setImageResource(R.drawable.ic_rainfall)
                "Drizzle" -> dateImageView.setImageResource(R.drawable.ic_light_rain)
                "Snow" -> dateImageView.setImageResource(R.drawable.ic_snow_sleet)
                "Atmosphere" -> dateImageView.setImageResource(R.drawable.ic_partly_cloudy)
            }


        }
    }


}