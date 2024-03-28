package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class ForecastDTO(
    @SerializedName("dt")
    var date: Long,
    var main: MainWeatherDTO,
    var weather: List<WeatherDTO>,
    var clouds: CloudsDTO,
    var wind: WindDTO,
    var visibility: Int,
    var pop: Double,
    var name: String,
    var sys: Sys,
)