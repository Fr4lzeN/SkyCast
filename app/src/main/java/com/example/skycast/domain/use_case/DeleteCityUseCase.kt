package com.example.skycast.domain.use_case

import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeleteCityUseCase(
    private val forecastRepository: ForecastRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun execute(forecast: Forecast, forecasts: List<Forecast>?): List<Forecast> {
        return withContext(coroutineDispatcher) {
            if (forecasts == null) return@withContext emptyList()
            val newForecasts = forecasts.toMutableList()
            if (newForecasts.removeIf { it.cityName == forecast.cityName }) {
                launch { forecastRepository.deleteForecast(forecast.cityName) }
            }
            return@withContext newForecasts
        }
    }

}