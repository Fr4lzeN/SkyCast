package com.example.skycast.data.repository

import com.example.skycast.data.data_source.dao.ForecastDao
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastRepositoryImpl(
    private val forecastDao: ForecastDao,
) : ForecastRepository {
    override suspend fun insertForecast(forecast: Forecast) {
        withContext(Dispatchers.IO) {
            forecastDao.insertForecast(forecast)
        }
    }

    override suspend fun insertAllForecasts(forecasts: List<Forecast>) {
        withContext(Dispatchers.IO) {
            forecastDao.insertAllForecasts(forecasts)
        }
    }

    override suspend fun getForecast(cityName: String): Forecast {
        return withContext(Dispatchers.IO) {
            forecastDao.getForecast(cityName)
        }
    }

    override suspend fun getAllForecasts(): List<Forecast> {
        return withContext(Dispatchers.IO) {
            forecastDao.getAllForecasts()
        }
    }

    override suspend fun deleteForecast(cityName: String) {
        withContext(Dispatchers.IO) {
            forecastDao.deleteForecast(cityName)
        }
    }


}