package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class DayWeatherDTO(
    @SerializedName("dt")
    var date: Long,
    var main: MainWeatherDTO,
    var weather: WeatherDTO,
    var clouds: CloudsDTO,
    var wind: WindDTO,
    var visibility: Int,
    var pop: Double,
    @SerializedName("dt_txt")
    var forecastTime: String,
)
