package com.example.skycast.domain.model

import androidx.room.Embedded

data class DayWeather(
    val date: Long,
    val cloudiness: Int,
    @Embedded
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    @Embedded
    val weather: Weather,
    @Embedded
    val mainWeather: MainWeather,
    val forecastTime: String,
){
}

