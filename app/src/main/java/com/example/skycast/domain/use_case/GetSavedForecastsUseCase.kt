package com.example.skycast.domain.use_case

import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSavedForecastsUseCase(
    private val forecastRepository: ForecastRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun execute(): List<Forecast> {
        return withContext(coroutineDispatcher) {
            forecastRepository.getAllForecasts()
        }
    }

}