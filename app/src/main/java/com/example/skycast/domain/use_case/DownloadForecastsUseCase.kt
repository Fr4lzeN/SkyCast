package com.example.skycast.domain.use_case

import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository
import com.example.skycast.domain.repository.WeatherApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadForecastsUseCase(
    private val weatherApiRepository: WeatherApiRepository,
    private val forecastRepository: ForecastRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun execute(forecasts: List<Forecast>?): List<Forecast> {
        return withContext(coroutineDispatcher) {
            if (forecasts == null) return@withContext emptyList()
            val updatedList = forecasts.toMutableList()
            launch {
                updatedList.forEachIndexed { index, forecast ->
                    launch {
                        val response = weatherApiRepository.getForecast(forecast.cityName)
                        if (response.isSuccess) {
                            updatedList[index] =
                                response.getOrNull()!!.copy(date = updatedList[index].date)
                        }
                    }
                }
            }.join()
            updatedList.sortBy { it.date }
            launch { forecastRepository.insertAllForecasts(updatedList) }
            updatedList
        }
    }

}