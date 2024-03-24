package com.example.skycast.data.repository

import com.example.skycast.data.data_source.dao.ForecastDao
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.Forecast
import com.example.skycast.domain.repository.ForecastRepository

class ForecastRepositoryImpl(
    private val forecastDao: ForecastDao,
): ForecastRepository {


    override suspend fun addForecast(forecast: Forecast) {
        forecastDao.addForecast(forecast)
    }

    override suspend fun deleteCity(city: City) {
        forecastDao.deleteCity(city)
    }

    override suspend fun getCities(): List<City> {
        return forecastDao.getCities()
    }

    override suspend fun getForecast(cityName: String): Result<Forecast> {
        return Result.success(forecastDao.getForecast(cityName))
    }

    override suspend fun updateForecast(forecast: Forecast) {
        forecastDao.updateForecast(forecast)
    }
}