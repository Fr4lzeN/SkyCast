package com.example.skycast.domain.repository

import com.example.skycast.domain.model.Forecast

interface WeatherApiRepository {

    suspend fun getForecast(
        cityName: String,
    ): Result<Forecast>

    suspend fun getForecast(
        longitude: Double,
        latitude: Double,
    ): Result<Forecast>

}