package com.example.skycast.data.dto.weather

import com.google.gson.annotations.SerializedName

data class MainWeatherDTO(
    @SerializedName("temp")
    var temperature: Double,
    @SerializedName("feels_like")
    var feelsLikeTemperature: Double,
    @SerializedName("temp_min")
    var temperatureMin: Double,
    @SerializedName("temp_max")
    var temperatureMax: Double,
    var pressure : Int,
    @SerializedName("sea_level")
    var seaLevel: Int,
    @SerializedName("grnd_level")
    var groundLevel: Int,
    var humidity: Int,
)
