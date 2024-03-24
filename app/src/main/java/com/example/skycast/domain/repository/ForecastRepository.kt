package com.example.skycast.domain.repository

import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.Forecast

interface ForecastRepository {

    suspend fun addForecast(forecast: Forecast)

    suspend fun deleteCity(city: City)

    suspend fun getCities(): List<City>

    suspend fun getForecast(cityName: String): Result<Forecast>

    suspend fun updateForecast(forecast: Forecast)

}