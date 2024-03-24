package com.example.skycast.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DayWeather(
    @ColumnInfo("day_weather_id")
    @PrimaryKey(true)
    var id: Long = 0,
    @ColumnInfo("city_name")
    val cityId: String,
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
)
