package com.example.skycast.domain.model

data class MainWeather(
    val temperature: Int,
    val feelsLikeTemperature: Int,
    val temperatureMin: Int,
    val temperatureMax: Int,
    val pressure: Int,
    val humidity: Int,
)
