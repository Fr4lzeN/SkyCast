package com.example.skycast.presentation.view_models

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

    private val _selectedForecast = MutableStateFlow<Forecast?>(null)
    val selectedForecast = _selectedForecast.asStateFlow()


    fun switchForecast(currentForecast: Forecast?, next: Boolean = true) {
        viewModelScope.launch(Dispatchers.Default) {
            val list = _forecasts.value ?: return@launch
            val index = list.indexOf(currentForecast)
            if (index == -1) return@launch
            if (next) {
                if (index < list.lastIndex) {
                    _selectedForecast.update { list[index + 1] }
                }
            } else {
                if (index > 0) {
                    _selectedForecast.update { list[index - 1] }
                }
            }
        }
    }

    fun selectForecast(forecast: Forecast) {
        _selectedForecast.update { forecast }
    }

    fun getForecast() {
        viewModelScope.launch(Dispatchers.Default) {
            val list = forecastRepository.getAllForecasts()
            if (list.isEmpty()) {
                _hasCities.emit(false)
            } else {
                _forecasts.update {
                    list
                }
                _selectedForecast.update {
                    list[0]
                }
                _hasCities.emit(true)
            }
            updateForecast()
        }
    }

    fun updateForecast() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = _forecasts.value ?: return@launch
            val updatedForecast = mutableListOf<Forecast>()
            launch {
                list.forEach {
                    launch {
                        val response = weatherApiRepository.getForecast(it.cityName)
                        if (response.isSuccess) {
                            val forecast = response.getOrThrow()
                            updatedForecast.add(forecast.copy(date = it.date))
                        } else {
                            updatedForecast.add(it)
                        }
                    }
                }
            }.join()
            updatedForecast.sortBy { it.date }
            val currentForecast = _selectedForecast.value
            val index = updatedForecast.indexOfFirst {
                it.cityName == currentForecast?.cityName
            }
            _selectedForecast.update {
                if (index != -1) {
                    updatedForecast[index]
                } else {
                    updatedForecast.firstOrNull()
                }
            }
            _forecasts.update { updatedForecast }
            launch { forecastRepository.insertAllForecasts(updatedForecast) }
        }
    }

    fun deleteForecast(forecast: Forecast) {
        viewModelScope.launch(Dispatchers.Default) {
            var list: MutableList<Forecast>
            _forecasts.update {
                if (it == null) return@update null
                list = it.toMutableList()
                list.remove(forecast)
                list
            }
            if (_selectedForecast.value == forecast) {
                _selectedForecast.update { null }
            }
            launch { forecastRepository.deleteForecast(forecast.cityName) }
        }
    }

    fun addCity(cityName: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val response = weatherApiRepository.getForecast(
                cityName,
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