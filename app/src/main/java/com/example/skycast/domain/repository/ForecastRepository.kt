package com.example.skycast.domain.repository

import com.example.skycast.domain.model.Forecast

interface ForecastRepository {

    suspend fun insertForecast(forecast: Forecast)
    suspend fun insertAllForecasts(forecasts: List<Forecast>)

    suspend fun getForecast(cityName: String): Forecast

    suspend fun getAllForecasts(): List<Forecast>

    suspend fun deleteForecast(cityName: String)

}