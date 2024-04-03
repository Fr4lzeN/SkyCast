package com.example.skycast.data.repository

import com.example.skycast.data.data_source.dao.ForecastDao
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastRepositoryImpl(
    private val forecastDao: ForecastDao,
    private val coroutinesDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ForecastRepository {
    override suspend fun insertForecast(forecast: Forecast) {
        withContext(coroutinesDispatcher) {
            forecastDao.insertForecast(forecast)
        }
    }

    override suspend fun insertAllForecasts(forecasts: List<Forecast>) {
        withContext(coroutinesDispatcher) {
            forecastDao.insertAllForecasts(forecasts)
        }
    }

    override suspend fun getForecast(cityName: String): Forecast {
        return withContext(coroutinesDispatcher) {
            forecastDao.getForecast(cityName)
        }
    }

    override suspend fun getAllForecasts(): List<Forecast> {
        return withContext(coroutinesDispatcher) {
            forecastDao.getAllForecasts()
        }
    }

    override suspend fun deleteForecast(cityName: String) {
        withContext(coroutinesDispatcher) {
            forecastDao.deleteForecast(cityName)
        }
    }


}