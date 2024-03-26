package com.example.skycast.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Forecast(
    val date: Long,
    @Embedded
    val main: MainWeather,
    @Embedded
    val weather: Weather,
    val cloudiness: Int,
    @Embedded
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    @PrimaryKey
    val cityName: String,
    @Embedded
    val cityInfo: City,
)