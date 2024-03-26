package com.example.skycast.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository
import com.example.skycast.domain.repository.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherApiRepository: WeatherApiRepository,
    private val forecastRepository: ForecastRepository,
) : ViewModel() {

    private val _hasCities = MutableStateFlow<Boolean?>(null)
    val hasCities = _hasCities.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _forecasts = MutableStateFlow<List<Forecast>?>(null)
    val forecasts = _forecasts.asStateFlow()

    fun getForecast() {
        viewModelScope.launch(Dispatchers.Main) {
            val list = forecastRepository.getAllForecasts()
            if (list.isEmpty()) {
                _hasCities.emit(false)
            } else {
                _forecasts.update {
                    list
                }
                _hasCities.emit(true)
            }
            val updatedForecast = mutableListOf<Forecast>()
            launch {
                list.forEach {

                    launch {
                        val response = weatherApiRepository.getForecast(
                            it.cityName,
                            "dc8ecec968c5f0fa2162b50eb1cce678",
                            "metric",
                            "ru"
                        )
                        if (response.isSuccess) {
                            updatedForecast.add(response.getOrThrow())
                        } else {
                            updatedForecast.add(it)
                        }
                    }
                }
            }.join()
            _forecasts.update { updatedForecast }
        }
    }

    fun addCity(cityName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val response = weatherApiRepository.getForecast(
                cityName, "dc8ecec968c5f0fa2162b50eb1cce678",
                "metric",
                "ru"
            )
            if (response.isSuccess) {
                val forecast = response.getOrThrow()
                launch { forecastRepository.insertForecast(forecast) }
                if (_forecasts.value?.indexOfFirst {
                        forecast.cityName == it.cityName
                    } != -1) {
                    _errorMessage.emit("Этот город уже сохранен")
                    return@launch
                }
                _forecasts.update {
                    val list = it?.toMutableList()
                    list?.add(forecast)
                    list
                }
            } else {
                _errorMessage.emit("Ошибка, попробуйте позже")
            }
        }
    }
}