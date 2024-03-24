package com.example.skycast.presentation

import androidx.lifecycle.ViewModel
import com.example.skycast.domain.repository.ForecastRepository
import com.example.skycast.domain.repository.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherApiRepository: WeatherApiRepository,
    private val forecastRepository: ForecastRepository,
): ViewModel() {

}