package com.example.skycast.domain.repository

import com.example.skycast.domain.model.Forecast

interface WeatherApiRepository {

    suspend fun getForecast(
        cityName: String,
        apikey: String,
        units: String = "standart",
        language: String = "en",
    ): Result<Forecast>

    suspend fun getForecast(
        longitude: Double,
        latitude: Double,
        apikey: String,
        units: String = "standart",
        language: String = "en",
    ): Result<Forecast>

}