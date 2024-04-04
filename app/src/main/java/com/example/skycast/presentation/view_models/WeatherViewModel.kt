package com.example.skycast.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.use_case.AddCityUseCase
import com.example.skycast.domain.use_case.DeleteCityUseCase
import com.example.skycast.domain.use_case.DownloadForecastsUseCase
import com.example.skycast.domain.use_case.GetSavedForecastsUseCase
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
    private val addCityUseCase: AddCityUseCase,
    private val deleteCityUseCase: DeleteCityUseCase,
    private val downloadForecastsUseCase: DownloadForecastsUseCase,
    private val getSavedForecastsUseCase: GetSavedForecastsUseCase,
) : ViewModel() {


    private val _errorMessage = MutableSharedFlow<AddCityUseCase.AddCityError>()
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
        viewModelScope.launch {
            val savedForecasts = getSavedForecastsUseCase.execute()
            _forecasts.update {
                savedForecasts
            }
            _selectedForecast.update { savedForecasts.firstOrNull() }
            updateForecast()
        }
    }

    fun updateForecast() {
        viewModelScope.launch {
            _forecasts.update {
                downloadForecastsUseCase.execute(_forecasts.value)
            }
        }
    }

    fun deleteForecast(forecast: Forecast) {
        viewModelScope.launch {
            val updatedForecasts = deleteCityUseCase.execute(forecast, _forecasts.value)
            if (forecast == _selectedForecast.value) {
                _selectedForecast.update { updatedForecasts.firstOrNull() }
            }
            _forecasts.update { updatedForecasts }
        }
    }

    fun addCity(cityName: String) {
        viewModelScope.launch {
            val result = addCityUseCase.execute(cityName, _forecasts.value)
            if (result.isSuccess()){
                _forecasts.update { result.getValue() }
            }else{
                _errorMessage.emit(result.getError())
            }
        }
    }
}