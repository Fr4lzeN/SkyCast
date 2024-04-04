package com.example.skycast.domain.use_case

import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository
import com.example.skycast.domain.repository.WeatherApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCityUseCase(
    private val weatherApiRepository: WeatherApiRepository,
    private val forecastRepository: ForecastRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend fun execute(
        cityName: String,
        forecasts: List<Forecast>? = emptyList()
    ): AddCityResult<List<Forecast>> {
        return withContext(coroutineDispatcher) {
            if (forecasts?.find { it.cityName == cityName } != null) return@withContext AddCityResult(
                error = AddCityError.ALREADY_EXISTS
            )
            val response = weatherApiRepository.getForecast(cityName)
            if (response.isFailure) {
                return@withContext AddCityResult(error = AddCityError.WRONG_NAME)
            }
            val forecast = response.getOrNull()!!
            val newForecasts = forecasts?.toMutableList() ?: mutableListOf()
            newForecasts.add(forecast)
            launch { forecastRepository.insertAllForecasts(newForecasts) }
            return@withContext AddCityResult(value = newForecasts)
        }
    }

    enum class AddCityError {
        WRONG_NAME,
        ALREADY_EXISTS,
    }

    class AddCityResult<T>(
        private val value: T? = null,
        private val error: AddCityError? = null,
    ) {

        fun isSuccess() = error == null

        fun getValue() = value!!

        fun getValueOrNull() = value

        fun getError() = error!!

        fun getErrorOrNull() = error

    }

}