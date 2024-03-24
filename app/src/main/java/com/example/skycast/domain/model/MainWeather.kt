package com.example.skycast.domain.model

data class MainWeather(
    val temperature: Double,
    val feelsLikeTemperature: Double,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val pressure: Int,
    val humidity: Int,
)
